/*cryptTEA.c*/

#include <stdio.h>
#include <string.h>

extern void encrypt(unsigned char block[], unsigned char key[]);

int main(int argc, char **argv)
{
    FILE *fPlain, *fCiphered, *fKey;
    unsigned char buf[8];
    unsigned char key[16];
    int nr;

    if (argc != 4) {
        printf("Uso: %s fileWithKey fileDecoded fileEncoded \n", argv[0]);
        return 1;
    }

    fKey = fopen(argv[1], "rb");
	if( fKey == NULL){
		printf("Error in opening file with key\n");
		return 2;
	}

    fPlain = fopen(argv[2], "rb");
    if (fPlain == NULL) {
        printf("Error in opening file with plain content\n");
        fclose(fKey);
        return 3;
    }

    fCiphered = fopen(argv[3], "wb+");
    if (fCiphered == NULL) {
        printf("Error in opening file with ciphered contents\n");
        return 4;
    }
    // read key
    fread(key, sizeof(unsigned char), 16, fKey);
    fclose(fKey);

    while (1) {
      nr = fread(buf, sizeof(unsigned char), 8, fPlain);
        if (nr == 0) {
            break;
        }

        if (nr < 8) {
            for (int i = nr; i < 8; i++) {
            buf[i] = 0;
            }
        }

        encrypt(buf, key);
        fwrite(buf, sizeof(unsigned char), 8, fCiphered);

        if (nr < 8) {
            break;
        }
    }

    fclose(fPlain);
    fclose(fCiphered);

    return 0;
}