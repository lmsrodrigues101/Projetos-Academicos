.globl allocate         # void *allocate( heap *h, unsigned int size);
.globl deallocate       # void deallocate( void * p);

HEADER_SIZE=8           #size of space for memory region header
HDR_AVAIL_OFFSET=0      #Location of the "available" flag in the header
HDR_SIZE_OFFSET=4       #Location of the size field in the header
UNAVAILABLE=0           #space that has been given out
AVAILABLE= 1            #space that has been returned

HEAP_BASE_OFFSET=0        #Location of the heap base address field in the heap descript
HEAP_TOP_OFFSET=8         #Location of the heap top address field in the heap descriptor
HEAP_LIMIT=16             #Location of end address in the heap descriptor

NULL=-1

.section .note.GNU-stack,"",@progbits
.text


allocate:
# void *allocate( heap *h, unsigned int size );
#                      rdi              esi

    movq $0, %r8            # %r8 armazena o melhor bloco disponível encontrado (inicializado para NULL)
    movq HEAP_LIMIT(%rdi), %rdx          # %rdx = h->limit
    movq HEAP_BASE_OFFSET(%rdi), %rcx      # %rcx = h->base
    subq %rcx, %rdx             # %rdx = h->limit - h->base
    movl %edx, %r9d           # %r9d guarda a diferença (em 32 bits)

    movq HEAP_BASE_OFFSET(%rdi), %rax   # %rax vai fazer a procura de onde o novo bloco poderá ser alocado, comeca em base

loop:
    cmpq HEAP_TOP_OFFSET(%rdi), %rax # se %rax >= h->top, 
    jge scan_end

CondB1:
    cmpl $AVAILABLE, HDR_AVAIL_OFFSET(%rax)  # verifica se o bloco atual está disponível (b->available == 1)
    jne continueLoop # se não estiver, vai para o próximo bloco

CondB2:
    movl HDR_SIZE_OFFSET(%rax), %ecx        # %ecx = b->size
    cmpl %esi, %ecx           # compara b->size com o tamanho desejado
    jl continueLoop               # se b->size < size, vai para o próximo bloco

    cmpl %r9d, %ecx            # verifica se o bloco é melhor (menor) que o melhor encontrado até agora
    jl update_best          # se for melhor, atualiza o melhor bloco

continueLoop:
    movl HDR_SIZE_OFFSET(%rax), %edx     # %edx = b->size
    addq %rdx, %rax                # avança para o fim do bloco atual
    addq $HEADER_SIZE, %rax                # avança para o início do próximo bloco (próximo header)
    jmp loop               # repete o processo

update_best:
    movq %rax, %r8        # guarda o ponteiro para o melhor bloco até agora
    movl %ecx, %r9d        # guarda o tamanho do melhor bloco
    jmp continueLoop    # continua varredura

scan_end:
    testq %r8, %r8     # verifica se encontrou algum bloco adequado
    jnz use_best # se encontrou, usa o melhor bloco

create:
    movq HEAP_TOP_OFFSET(%rdi), %rax        # %rax = h->top (início do novo bloco)
    movl $HEADER_SIZE, %ecx                 # %ecx = HEADER_SIZE
    addl %esi, %ecx                         # %ecx = HEADER_SIZE + size
    lea (%rax, %rcx), %rdx                  # %rdx = novo valor de h->top após alocação
    cmpq %rdx, HEAP_LIMIT(%rdi)             # verifica se há espaço suficiente no heap
    jl end                                                       # caso contrário, retorna NULL
    movl $UNAVAILABLE, (%rax)               # marca o bloco como indisponível (b->available = 0)
    movl %esi, HDR_SIZE_OFFSET(%rax)        # define o tamanho do bloco (b->size = size)
    movq %rdx, HEAP_TOP_OFFSET(%rdi)        # atualiza h->top para refletir novo fim do heap
    addq $HEADER_SIZE, %rax                 # retorna ponteiro para área de dados (após o cabeçalho)
    retq

use_best:
    movl $UNAVAILABLE, HDR_AVAIL_OFFSET(%r8)  # marca o melhor bloco como indisponível
    leaq HEADER_SIZE(%r8), %rax               # retorna ponteiro para área de dados do bloco
    retq

end:
    movq $NULL, %rax        
    retq

deallocate:
# void deallocate( void * p );
#                       rdi

    leaq -HEADER_SIZE(%rdi), %rax           # %rax aponta para o início do bloco (cabeçalho)
    movl $AVAILABLE, HDR_AVAIL_OFFSET(%rax) # marca o bloco como disponível
    retq
