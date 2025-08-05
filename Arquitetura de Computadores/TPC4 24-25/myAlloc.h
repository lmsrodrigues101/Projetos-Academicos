typedef struct {
  void *base;
  void *top;
  unsigned long int *limit;
} heap;

typedef struct{
  unsigned int available;
  unsigned int size;
} block;

void *allocate (heap *h, unsigned int size);
void deallocate (void * p);
