/*
CPU-Mem architecture
AC 2024/25  LEI FCT/UNL

Memory 4096 16-bit data words
12 bit addresses (max memory 2^12)
1 acumulator, no flags

16 bit instruction
    4 bits 15-12 opcode
    8 bits signed value or address
*/
#include <stdio.h>


extern unsigned short int Mem[];
extern short int ac;

void dorun(){
    unsigned short int pc;  // program counter or intruction pointer
    unsigned short int ir;  // instruction register

    unsigned char opcode;
	short addressOrValue;
	
    pc = 0;
    while( 1 ) {
        ir = Mem[pc];                  // FETCH
        opcode = (unsigned char)(ir >> 12);             // DECODE	
        addressOrValue =  ir & 0x0fff;
           
        switch( opcode ){              // EXECUTE
            case 0x00:   /* HALT */
                printf("HALT instruction executed\n");
                return;

             case 0x01:   /* LOAD */
                ac = Mem[addressOrValue];
		pc = pc + 1;
                break;
				
            case 0x02:   /* STORE */
  
				
            case 0x03:   /* ADD */
  

            case 0x04:   /* SUB */
   

            case 0x05:   /* MUL */
  

            case 0x06:   /* DIV */
   

            case 0x07:   /* JMP */
   

            case 0x08:   /* JZ */
   

            case 0x09:   /* JN */
  

            case 0x0A:   /* CALL */
  

            case 0x0B:   /* RETURN */
  

            case 0x0C:   /* LDI */
 

            case 0x0F:   /* NOP */
                pc = pc + 1;
                break;
	     
            default:
                printf("Invalid instruction!\n");
                return;
        }
    }
}
