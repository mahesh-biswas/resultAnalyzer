import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class subjectMapper {
    private static final String FILENAME = "be";

    public static void main(String[] args) {
        try {
            PdfReader reader = new PdfReader(FILENAME+".pdf");
            int x = reader.getNumberOfPages();
            for (int i=1;i<2;i++){
                print("hello");
                String txt = PdfTextExtractor.getTextFromPage(reader, i);
//                println(txt);
                String[] studentPerPage = txt.split("[.]{10}");
                print(studentPerPage.length+"<<");
                int a=0;
                for (String tmp : studentPerPage){
                    if(!tmp.isEmpty()) {
//                        println(">>>>>>>>>>\t" + (++a) + "\n" + tmp + "\n<<<<<<<<<<<<<<<<<<");
                        if(a>=1)
                            allSubjects(tmp);
                        a++;
                    }
                }


            }
        }catch (Exception e){
            e.getStackTrace();
        }
    }
    private static void allSubjects(String rawStudent){
        println(":::::::\n"+rawStudent);
        String subs[] = rawStudent.split("\\n");
        print("length: "+subs.length);
        int a=0;
        for (String sub:subs){
            println((++a)+":\t"+sub.trim());
/*
 214457I 1
 310250 0
 310251 0
 310241 0
 310252 0
 310242 0
 310253 0
 310243 0
 214457F 1
 310254 0
 310244 0
 310255 1
 310245 0
 310256 4
 310246 3
 310257 2
 310247 4
 310258 1
 310248 4
 310259E 1
 304217A 1
 310260E 1
*
* */
        }
    }
    private static void print(String message){
        System.out.print(message);
    }
    private static void println(String message){
        System.out.println(message);
    }
}
