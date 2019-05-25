import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    private static String FILENAME = "te.pdf";

    private static ArrayList<Student> studentArrayList = new ArrayList<>();
    public static void main(String[] args) {
        try {
            PdfReader reader = new PdfReader(FILENAME);
            int x = reader.getNumberOfPages();
            log("page count",x+"");
            for (int i=x;i<=x;i++){
                String txt = PdfTextExtractor.getTextFromPage(reader,125);
                SplitStudentsPerPage(txt);
            }
//            display();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void display() {
        for (Student tmp: studentArrayList){
            printf(tmp.toString());
        }
    }

    static void SplitStudentsPerPage(String page){
        String arr[] = page.split("....................................................................................................");
//            printf(txt);
//        log("count",arr.length+"");
        int i=0;
        for(String tmp: arr){
//                printf(tmp);
            sem = 1;
            if(i==1)
                studentArrayList.add(ExtractDetails(tmp));
//                output+=ExtractDetails(tmp).trim()+"\n";
            i++;
        }
    }
    static Student ExtractDetails(String studentString){
        Student student = new Student();
        String []tmp = studentString.split("\\n");
//        printf(tmp.length+"");
//        printArr(tmp);
        int x=0,y=0;
        for(int i=0;i<tmp.length;i++){
            String line = tmp[i];
            if(line.trim().length()==0){
                y++;
                continue;
            }else{
                x++;
            }
            if(x>4 && x<tmp.length-y-1){
                log(x+":\t",line);
                String word[] = line.split("\\s+");
                parseLine(word,student);
                printf(sem+"<=Semester");
//                printArr(word);
            }else if(x>4){
                log(x+"!!:\t",line);
            }
        }
        if(sem==2){
            Semester semester =new Semester();
            semester.setSemNo(sem);
            semester.setSubjectList(subjectArrayList);
            semesters.add(semester);
            subjectArrayList.clear();
            sem=1;
        }
        System.out.println("Total subject count: "+semesters.size());
        System.out.println("sem1 subjects count: "+semesters.get(0).getSubjectList().size());
        System.out.println("sem2 subjects count: "+subjectArrayList.size());
        printSemester();
//        printf(Student);
//        printf("\n");

        return null;
    }
    static void printSemester(){
        for(Semester semester:semesters){
            plainPrint(semester.toString());
            for(subject sub:semester.getSubjectList()){
                plainPrint(sub.toString());
            }
        }
    }
    static void plainPrint(String s){
        System.out.println(s);
    }
    static int sem = 1;
    static ArrayList<Semester> semesters = null;
    static ArrayList<subject> subjectArrayList = new ArrayList<>();
    private static void parseLine(String[] word, Student student) {
        String res = "";
        for (String tmp:word){

            if(tmp.length()!=0){
//                printf(tmp+" [[word_len:"+word.length);
                try{
                    if(tmp.contains("SEM")){
//                        printf("yes :"+tmp.substring(tmp.length()-1));
                        int tsem = Integer.parseInt(tmp.substring(tmp.length()-1));
                        if(tsem!=sem){
                            Semester semester =new Semester();
                            semester.setSemNo(sem);
                            semester.setSubjectList(subjectArrayList);
                            if(sem==1){
                                semesters = new ArrayList<>();
                            }
                            semesters.add(semester);
                            subjectArrayList.clear();
                            sem=tsem;
                        }
                    }else if(word.length>=13){
                        subject sub = getSubject(word);
                        if(sub!=null){
                            subjectArrayList.add(sub);
                        }
                        return;
                    }
                }catch(Exception e){

                }


            }else{
//                printf(tmp+" [[word_len:"+word.length);
            }
        }
    }

    private static subject getSubject(String[] word) {
//        System.out.println("test::\t");
//        printArr(word);
        subject subject = new subject(word[1]);
        int index=0;
        if(!word[2].equalsIgnoreCase("*")){
            index=1;
        }
        System.out.println(index);
        subject.setTotP(word[9-index]);
        subject.setCrd(getMarks(word[10-index]));
        subject.setGrd(word[11-index]);
        subject.setGPts(getMarks(word[12-index]));
        subject.setCPts(getMarks(word[13-index]));
        /***
         *      Varying Part
         * ****/
        if(isMarks(word[3-index])){
            subject.setIN(getMarks(word[3-index]));
        }
        if(isMarks(word[4-index])){
            subject.setTH(getMarks(word[4-index]));
        }
        if(isMarks(word[6-index])){
            subject.setTW(getMarks(word[6-index]));
        }
        if(isMarks(word[7-index])){
            subject.setPR(getMarks(word[7-index]));
        }
        if(isMarks(word[8-index])){
            subject.setOR(getMarks(word[8-index]));
        }
        System.out.println(subject.toString());

        return subject;
    }
    static boolean isMarks(String test){
        if(test.trim().equalsIgnoreCase("-------")){
            return false;
        }else{
            return true;
        }
    }
    static Student ExtractDetails0(String string){
        String all[] = string.split("  ");
        String examno=all[0].trim(), name= all[1].trim();
//        printArr(all);
        String score[] = string.split(":");
        printf("score[2]",score[1].substring(0,2).trim(),"Printing Score:::");
//        printArr(score);
        int index = 2;
        boolean reappeared = false;
        printf(score.length+"");
        if(score.length>4){
            index++;
            reappeared =true;
        }
        Student stu =new Student(
                examno,
                name,
                reappeared
        );
        String tmp[] = string.split("\\n");
        printArr(tmp);
        ArrayList<Semester> sems = stu.getSemesterList();
        if(sems!=null){
            Semester sem = new Semester();
            sem.setSemNo(Integer.parseInt(score[1].substring(0,1)));
            sem.setGpa(score[index].split(",")[0].trim());
            ArrayList<subject> subjects = sem.getSubjectList();
                subject sub = new subject(all[27]);
                sub.setIN(getMarks(all[28]));
                sub.setTH(getMarks(all[29]));
                sub.setTW(getMarks(all[31]));
                sub.setPR(getMarks(all[32]));
                sub.setOR(getMarks(all[33]));
                sub.setTotP(getMarks(all[34])+"");
                sub.setCrd(getMarks(all[35]));
                sub.setGPts(getMarks(all[37]));
                sub.setCPts(getMarks(all[38]));
                Main.printf(sub.toString());
                sem.addSubject(sub);
            sems.add(new Semester());
        }else {
            JOptionPane.showMessageDialog(null,"null");
        }
        if(stu == null){
            printArr(score);
        }
        return stu;

    }

    private static int getMarks(String s) {
        String tmp[] = s.split("/");
//        printf(s);
//        printf(tmp);
        try {
            return Integer.parseInt(tmp[0]);
        }catch (Exception e){
            return 00;
        }
    }

    private static void printf(String... params) {
        int x=0;
        for(String par:params){
            System.out.print(x++ +" ["+par+"]\t");
        }
    }

    static void printArr(String[] arr){
        int x=0;
        printf("arrLen:\t"+arr.length);
        for(String tmp:arr){
            if(tmp.length()==0){
                log(x++ +"b:\t",tmp);
            }else {
                log(x++ +"!b:\t",tmp);
            }
        }
    }
    static void log(String tag, String msg){
        System.out.println(tag+""+msg);
    }

    static void printf(String msg){
        System.out.println(msg+"\t len:"+msg.length());
    }
    static void hl(){
        printf("....................................................................................................");
    }
    static String hl2s(){
        return "....................................................................................................";
    }
}
