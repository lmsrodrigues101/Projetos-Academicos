DELTA = 0x9E3779B9
.global encrypt, decrypt
.section .note.GNU-stack,"",@progbits
.text
############################# tea ###################################
encrypt:
						# %rdi points to buf
						# %rsi points to key
 		pushq   %rbp
   
		movl    (%rdi),%eax   # definir y = text [0]
		movl    4(%rdi),%ebx # definir z = text [1]

		movl    $DELTA,%r8d # definir delta = 0x9e3779b9
		movl    $0,%r9d      # definir sum = 0
		movl    $0,%ecx     # counter do for
		
		jmp _EncryptLoop
		
	
############################# detea ###################################	
decrypt:
	
						# %rdi points to buf
						# %rsi points to key
		pushq   %rbp
    
		movl    (%rdi),%eax   # definir y = text [0]
		movl    4(%rdi),%ebx # definir z = text [1]

		movl    $DELTA,%r8d # definir delta = 0x9e3779b9
		movl    %r8d,%r12d # r12x = delta
		shl     $5,%r12d  # delta << 5;
		movl    %r12d,%r9d  # sum = delta << 5;
		movl    $0,%ecx     # counter do for
	  jmp .DecryptLoop
	
	
_EncryptLoop: 
		cmpl $32,%ecx  # compara rcx ... 32
		jge  .EncryptEnd     # acaba se n >= 32

		addl  %r8d,%r9d  # sum += delta

		movl  %ebx,%edx		# edx = z 
		shl   $4,%edx  # z<<4
		addl  (%rsi),%edx  # z<<4  +  k[0]
		movl  %ebx,%r10d  # r10x = z
		addl  %r9d,%r10d   # z+sum
		movl  %ebx,%r11d   # r11x = z
		shr   $5,%r11d  # z >> 5
		addl  4(%rsi),%r11d  # z>>5  +  k[1]
		xor   %r10d,%edx  # ((z << 4) + k[0]) ^ (z+sum)
		xor   %r11d,%edx # ((z << 4) + k[0]) ^ (z+sum) ^ ((z >> 5) + k[1])
		addl  %edx,%eax  # y += ((z << 4) + k[0]) ^ (z+sum) ^ ((z >> 5) + k[1])

		movl  %eax,%edx		# edx = y
		shl   $4,%edx  # y<<4
		addl  8(%rsi),%edx  # y<<4  +  k[2]
		movl  %eax,%r10d  # r10x = y
		addl  %r9d,%r10d   # y+sum
		movl  %eax,%r11d   # r11x = y
		shr   $5,%r11d  # y >> 5
		addl  12(%rsi),%r11d  # y>>5  +  k[3]
		xor   %r10d,%edx  # ((y << 4) + k[2]) ^ (y+sum)
		xor   %r11d,%edx # ((y << 4) + k[2]) ^ (y+sum) ^ ((y >> 5) + k[3])
		addl  %edx,%ebx  # z += ((y << 4) + k[2]) ^ (y+sum) ^ ((y >> 5) + k[3])
		
		incl  %ecx
		jmp _EncryptLoop

.EncryptEnd:
		movl  %eax,(%rdi)  # text[0] = y
		movl 	%ebx,4(%rdi)  # text[1] = z
		popq %rbp
 		retq

.DecryptLoop:
	  cmpl $32,%ecx  # compara rcx ... 32
		jge  .DecryptEnd      # acaba se n >= 32
		movl  %eax,%edx		# edx = y
		shl   $4,%edx  # y<<4
		addl  8(%rsi),%edx  # y<<4  +  k[2]
		movl  %eax,%r10d  # r10x = y
		addl  %r9d,%r10d   # y+sum
		movl  %eax,%r11d   # r11x = y
		shr   $5,%r11d  # y >> 5
		addl  12(%rsi),%r11d  # y>>5  +  k[3]
		xor   %r10d,%edx  # ((y << 4) + k[2]) ^ (y+sum)
		xor   %r11d,%edx # ((y << 4) + k[2]) ^ (y+sum) ^ ((y >> 5) + k[3])
		subl  %edx,%ebx  # z -= ((y << 4) + k[2]) ^ (y+sum) ^ ((y >> 5) + k[3])

		movl  %ebx,%edx		# edx = z 
		shl   $4,%edx  # z<<4
		addl  (%rsi),%edx  # z<<4  +  k[0]
		movl  %ebx,%r10d  # r10x = z
		addl  %r9d,%r10d   # z+sum
		movl  %ebx,%r11d   # r11x = z
		shr   $5,%r11d  # z >> 5
		addl  4(%rsi),%r11d  # z>>5  +  k[1]
		xor   %r10d,%edx  # ((z << 4) + k[0]) ^ (z+sum)
		xor   %r11d,%edx # ((z << 4) + k[0]) ^ (z+sum) ^ ((z >> 5) + k[1])
		subl  %edx,%eax  # y -= ((z << 4) + k[0]) ^ (z+sum) ^ ((z >> 5) + k[1])
		subl  %r8d,%r9d  # sum -= delta
		incl %ecx
		jmp .DecryptLoop

.DecryptEnd :
		movl  %eax,(%rdi)  # text[0] = y
		movl 	%ebx,4(%rdi)  # text[1] = z
		popq %rbp
 		retq
