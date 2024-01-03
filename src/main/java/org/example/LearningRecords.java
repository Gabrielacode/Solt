package org.example;

public class LearningRecords {
    public static void main(String[] args) {
        int acase = 444 ;
        int result ;

 result =  switch (acase){

      case 144,244,344 -> acase = 1;
      case 444,544,644 ->  acase= 2;

      case 744,844,944 ->3;
      default-> 0;

  };
        System.out.println(result);
        System.out.println(acase);

        String textblock = """
                Text blocks support strings that
                span two or more lines and preserve
                    indentation. They reduce the
                       tedium associated with the
                    entry of long or complicated
                strings into a program.
              """;
        System.out.print(textblock);

        String blocv = """
                We can also write "double quotes" in text blocks.
                
                We can also do this ""Heloooo"
                
                """;
        System.out.println(blocv);

        String lineSuppressor = """
                This is a line suppressor\
                It stops lines from entering a new line
                This will start in a new line\
                but this will not
                """;
        System.out.println(lineSuppressor);
    }
}
