#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv) {
    if (argc != 2) {
        printf("Usage: %s <file>\n", argv[0]);
        return 2;  
    }
    
    FILE *file = fopen(argv[1], "rb");
    if (file == NULL) {
        printf("Error: could not open file %s\n", argv[1]);
        return 2;
    }

    int c;
    while ((c = fgetc(file)) != EOF) {
        if (!((c == 9) || (c == 10) || (c == 13) ||
        (c >= 32 && c <= 126) ||
        (c >= 160 && c <= 255))) {
            fclose(file);
            return 0; //é binario
        }
    }
    
    fclose(file);
    return 1; // é texto
}