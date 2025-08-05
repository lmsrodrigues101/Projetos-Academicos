#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/mman.h>
#include <unistd.h>

#include "myAlloc.h"
#define  MYNULL ((void *)-1)

heap h;

void debugBlockList( heap * ) ;
void allocate_init( heap *h, unsigned long int memSize ) ;

/**
 * teste allocate/deallocate
 **/
int main(int argc, char *argv[]) {
	unsigned int maxBlock;
	unsigned int heapSpace;

    if (argc != 3) {
        printf("%s totalSize maxBlock\n", argv[0]);
        exit(1);
    }
    heapSpace = atoi(argv[1]);
    maxBlock = atoi(argv[2]);

    allocate_init(&h, heapSpace);

    for (int s = 1; s < maxBlock; s *= 2) {
        printf("Trying to allocate %d bytes... ", s);
	    void *ptr = allocate(&h, s);
       	if (ptr == MYNULL){
       		printf("No more memory.\n");
       	}
        else {
            printf("Success. Let's deallocate it.\n");
            deallocate(ptr);
        }
    }
    debugBlockList(&h); 
    
    for (int s = maxBlock; s>0; s /= 2) {
	    printf("Trying to allocate %d bytes... ", s);
        void *ptr = allocate(&h, s);
        if (ptr == MYNULL){
            printf("No more memory.\n"); 
        }
        else {
            printf("Success.\n");
        }
    }
    debugBlockList(&h);

    return 0;
}

void debugBlockList( heap *h ){
    void *p = h->base;
    printf("\nAddress\t\t\tStatus\t\tSize\n");
    while(p < h->top){
        block *pt = p;
        printf("%p\t\t%s\t\t%u\n", pt, pt->available?"free":"busy", pt->size);
        p = p + sizeof(block) + pt->size;
    }
}

unsigned long long int adjustToMultipleOfPageSize( unsigned long int memSize ){
    unsigned long int pageSize = 4096;
	//printf("About to return %ld\n", (memSize + (pageSize -1)) &  ~(pageSize -1) );
    return (memSize + (pageSize -1)) &  ~(pageSize -1);
}

void allocate_init( heap *h, unsigned long int memSize ){
    unsigned long long int realSize = adjustToMultipleOfPageSize(memSize);
    void *addr = mmap(  0, realSize, PROT_READ | PROT_WRITE , MAP_ANONYMOUS | MAP_PRIVATE, -1, 0);
    if( addr == MAP_FAILED){
        perror("allocate_init");
        exit( 1 );
    }
    h->base = addr;
    h->top = addr;
    h->limit = (void *)((char*) addr + realSize); 
}