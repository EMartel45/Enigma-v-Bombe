//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
//Create a data base for each gear
    /*
    each gear has a int value for its position which will corispond to a ascii encryption value
    create a function that takes the user input for each pin selected on front of enigma machine.
    have a list of each of these pins and where they go ie a->g l->q and so on
    a letter can not be encrypted into itself ie a!->a 
     */
    // public static int encryptiontable(){
//}
    public static char ascii_encryption(char input_letter,int[] encryption_code){
        int encrypted = (int) input_letter;
      for (int i = 0; i < 3; i++) {
          encrypted = encrypted + encryption_code[i];
          while (encrypted > 122) {
              encrypted = encrypted - 26;
          }
      }

      encrypted = (int) input_letter + 4;

        for (int i = 0; i < 3; i++) {
            encrypted = encrypted + encryption_code[i];
            while (encrypted > 122) {
                encrypted = encrypted - 26;
            }
        }
        /*
        Pull from database for conversions a--> q h--> u etc...
         */

        return (char) encrypted;
    }



    public static void main(String[] args) {

String temp = "test input";

char[] tempArray = temp.toCharArray();

int[] encryptionarray = {2, 9, 5};

/*
use a string split here and break whole user input into chars then loop it and increase encryption array by 1 each time.
 */
        char[] encrypted =new char[tempArray.length];
        for (int i = 0; i<tempArray.length; i++){
            encrypted[i] = ascii_encryption(tempArray[i], encryptionarray);
            encryptionarray[0] = encryptionarray[0]+1;
        }
            //char encrypted = ascii_encryption(temp, encryption1, encryption2, encryption3);
        for (int i=0; i<encrypted.length; i++){
            boolean testVar = Character.isDigit(encrypted[i]);
            if (testVar){
                encrypted[i] = ' ';
            }
        }
        String finalEncrypt = String.valueOf(encrypted);

        System.out.printf("The test input was %s and the encrypted char is %s", temp, finalEncrypt);

    }


}