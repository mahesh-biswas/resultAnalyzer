import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("Duplicates")
public class MainObjectOriented {
    private static String FILENAME = "te";
    private static Set<String> subset = new TreeSet();
    private static HashMap<String,Integer> submapper = new HashMap<>();
    private static String absPath = "",parentDIR="",filename="";
    private int sem = 1;
    private ArrayList<Semester> semesters = null;
    private ArrayList<Student> studentArrayList = new ArrayList<>();
    private ArrayList<Year> YearList = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        try {
            JFileChooser fileChooserDialogue =  new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            int response = fileChooserDialogue.showOpenDialog(null);
            if(response != JFileChooser.APPROVE_OPTION){
                return;
            }
            absPath = fileChooserDialogue.getSelectedFile().getAbsolutePath();
            parentDIR = fileChooserDialogue.getSelectedFile().getParent();
            filename = fileChooserDialogue.getSelectedFile().getName().split("[.]")[0];
            printf(absPath,parentDIR,filename);
            MainObjectOriented obj = new MainObjectOriented();
            PdfReader reader = new PdfReader(absPath);
            int x = reader.getNumberOfPages();
            log("page count", x + "");
//            JOptionPane.showMessageDialog(null,"Task Started...","Began..",JOptionPane.INFORMATION_MESSAGE);
            plainPrintf("Processing..");
//            x = (int) x/4;
            for (int i = 1; i < x; i++) {
                String txt = PdfTextExtractor.getTextFromPage(reader, i);
                obj.SplitStudentsPerPage(txt);
                if(i%3==0)
                    plainPrintf(".");
            }
            obj.generateExcel();
//            System.out.println(submapper);
            JOptionPane.showMessageDialog(null,"Excel Sheet Created successfully","Done", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getColsOfSubject(int id) {
        /***
         *      Varying Part
         *      0 - IN
         *      1 - TW
         *      2 - PR
         *      3 - OR
         *      4 - TW + PR
         *      5 - TW + OR
         *      6 - PR + OR
         *
         * ****/
        if (id>10)  id -=10;
        switch (id){
            case 0: return "IN\nTH";
            case 1: return "TW";
            case 2: return "PR";
            case 3: return "OR";
            case 4: return "TW\nPR";
            case 5: return "TW\nOR";
            case 6: return "PR\nOR";
        }
        return null;
    }

    private void display() {
        for (Student tmp : studentArrayList) {
            printf(tmp.toString());
        }
    }

    void SplitStudentsPerPage(String page) {
        String arr[] = page.split("....................................................................................................");
        int i = 0;
        for (String tmp : arr) {
            sem = 1;
            if (i>0)
                studentArrayList.add(ExtractDetails(tmp));
            i++;
        }
    }

    Student ExtractDetails(String studentString) {
        Student student = new Student();
        String[] tmp = studentString.split("\\n");
        int x = 0, y = 0;
        for (int i = 0; i < tmp.length; i++) {
            String line = tmp[i];
//            log(i+":",line);
            if (line.trim().length() == 0) {
                y++;
                continue;
            } else {
                x++;
            }
            if (x > 4 && x < tmp.length - y - 1) {
                String word[] = line.split("\\s+");
                parseLine(word, student);
            } else if (x > 4) {
                String[] theFooter = studentString.split(": ");
                student.setSgpa(theFooter[1].split(",")[0]);

            } else if (x == 1) {
                String[] theHeader = line.split("  ");
                student.setName(theHeader[1]);
                student.setExamSeatNo(theHeader[0]);
            }
        }
        Semester semester = new Semester();
        semester.setSemNo(sem);
        ArrayList<subject> tmpList = new ArrayList<>(subjectArrayList);
        semester.setSubjectList(tmpList);
        if(sem==1){
            semesters = new ArrayList<>();
        }
        semesters.add(semester);
        subjectArrayList.clear();
        sem = 1;
        ArrayList<Semester> semesterArrayList = new ArrayList<>(semesters);
        student.setSemesterList(semesterArrayList);

        return student;
    }

    private void generateExcel() {
        String[] columns = {"Name", "Exam Seat No.", "SGPA"};
        StringBuilder subjectBuilder = new StringBuilder();
        StringBuilder builder = new StringBuilder();
        builder.append("Name\n");
        builder.append("Exam Seat No.\n");
        int insem = 0;
        for (String subjectKey: subset){
            int id = submapper.get(subjectKey);
            String cols = getColsOfSubject(id);
            String[] arrcols = cols.split("\n");
            subjectBuilder.append(subjectKey+"\n");
            for (String tmp : arrcols){
                builder.append(tmp+"\n");
                subjectBuilder.append("\n");
            }
            builder.append("Total\n");
        }
        subjectBuilder.append("\n");
        subjectBuilder.append("\n");
        builder.append("SGPA");
        String[] be = builder.toString().split("\n");
        Workbook workbook = new XSSFWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();
        Sheet sheet = workbook.createSheet("mahesh");
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        Font headerFontGreen = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.GREEN.getIndex());
        // Cell Style Green
        CellStyle cellStyleGreen = workbook.createCellStyle();
        cellStyleGreen.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
        cellStyleGreen.setFillPattern(FillPatternType.BIG_SPOTS);
        plainPrintf("g");
        //  CELL Style RED
        Font cellRedFont = workbook.createFont();
        cellRedFont.setColor(IndexedColors.WHITE.getIndex());
        cellRedFont.setBold(true);
        CellStyle cellStyleRed = workbook.createCellStyle();
        cellStyleRed.setFillBackgroundColor(IndexedColors.RED.getIndex());
        cellStyleRed.setFillPattern(FillPatternType.ALT_BARS);
//        cellStyleRed.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyleRed.setFont(cellRedFont);
        plainPrintf("r");

        headerCellStyle.setFont(headerFontGreen);
        plainPrintf(".");
        // Create a Row
        columns = be;
        String subjects[] = subjectBuilder.toString().split("\n");
        Row subjectList = sheet.createRow(0);
        int colindex=3;
        int insemSub = 7 + colindex;
        Row headerRow = sheet.createRow(1);

        int si=0;
        for(int ij = 0; ij < columns.length; ij++) {
            if(ij>1 && si<subjects.length)
                subjectList.createCell(ij).setCellValue(subjects[si++]);
            else
                subjectList.createCell(ij).setCellValue("");
            headerRow.createCell(ij).setCellValue(columns[ij]);
        }

        int rowNum = 2;
        int loading=0;
        for (Student student : studentArrayList) {
            loading++;
            if(loading%4==0)    plainPrintf(".");
            Row row = sheet.createRow(rowNum++);
            Cell cellName = row.createCell(0);
            cellName.setCellValue(student.getName());
            row.createCell(1).setCellValue(student.getExamSeatNo());
            String gpa = student.getSgpa();
            double x = -1;
            try{
                x = Double.parseDouble(gpa);
//                printf(":"+x+":");
            }catch(Exception e){
//                printf(":"+x+":");
            }
            Cell gpacell = row.createCell(columns.length-1);
            gpacell.setCellValue(gpa);
            if(x<0){
                cellName.setCellStyle(cellStyleRed);
                gpacell.setCellStyle(cellStyleRed);
            }
            int lastindex=0;
            Set<String> subdone = new HashSet();
            for (Semester semester : student.getSemesterList()) {
                int subjectNo = 0;
                int index=2+(semester.getSemNo()-1)*lastindex;
                if(semester.getSemNo()>1)
                    index-=2;
//                printf(index+"<-index");
                for (String tmp : subset) {
                    int position=0,sm=semester.getSemNo()-1;
                    subject sub = null;
                    for (subject tmpsub : semester.getSubjectList()){
                        if(tmp.trim().equalsIgnoreCase(tmpsub.getSubject().trim())){
                            sub = tmpsub;
                            break;
                        }
                        position++;
                    }
                    if(sub==null){
                        position = 0;
                        if((semester.getSemNo())<student.getSemesterList().size()){
                            for (subject tmpsub : student.getSemesterList().get(1).getSubjectList()){
                                if(tmp.trim().equalsIgnoreCase(tmpsub.getSubject().trim())){
                                    sm = 1;
                                    sub = tmpsub;
                                    break;
                                }
                                position++;
                            }
                        }else{
                            for (subject tmpsub : student.getSemesterList().get(0).getSubjectList()){
                                if(tmp.trim().equalsIgnoreCase(tmpsub.getSubject().trim())){
                                    sm = 0;
                                    sub = tmpsub;
                                    break;
                                }
                                position++;
                            }
                        }
                    }
                    if(subdone.contains(tmp.trim())){
                        continue;
                    }else{
                        subdone.add(tmp.trim());
                    }
                    if(sub==null){
                        row.createCell(index++).setCellValue("-");
                        row.createCell(index++).setCellValue("-");
                        subjectNo++;
                        continue;
                    }
                    /***
                     *      Varying Part
                     *      0 - IN
                     *      1 - TW
                     *      2 - PR
                     *      3 - OR
                     *      4 - TW + PR
                     *      5 - TW + OR
                     *      6 - PR + OR
                     *
                     * ****/
                    int id = submapper.get(sub.getSubject());
                    if(id == 0) {
                        Cell cellIN = row.createCell(index++);
                        cellIN.setCellValue(sub.getIN());
                        Cell cellTH = row.createCell(index++);
                        cellTH.setCellValue(sub.getTH());
                        int tot = sub.getIpt();
                        Cell cell = row.createCell(index++);
                        if(tot<40){
                            cell.setCellStyle(cellStyleRed);
                            cellIN.setCellStyle(cellStyleRed);
                            cellTH.setCellStyle(cellStyleRed);
//                            plainPrintf("r:");
                        }else{
//                            plainPrintf("g:");
//                            cell.setCellStyle(cellStyleGreen);
                        }
                        cell.setCellValue(tot);
                    }else if(id == 4){
                        row.createCell(index++).setCellValue(sub.getTW());
                        row.createCell(index++).setCellValue(sub.getPR());
                        row.createCell(index++).setCellValue(sub.add(sub.getTW(),sub.getPR()));
                    }else if(id == 5){
                        row.createCell(index++).setCellValue(sub.getTW());
                        row.createCell(index++).setCellValue(sub.getOR());
                        row.createCell(index++).setCellValue(sub.add(sub.getTW(),sub.getOR()));
                    }else if(id == 3){
                        row.createCell(index++).setCellValue(sub.getOR());
                        row.createCell(index++).setCellValue(sub.add(0,sub.getOR()));
                    }else if(id == 2){
                        row.createCell(index++).setCellValue(sub.getPR());
                        row.createCell(index++).setCellValue(sub.add(0,sub.getPR()));
                    }else if(id == 1){
                        int tq = sub.getTW();
                        if (tq==0){
                            row.createCell(index++).setCellValue("PP");
                            row.createCell(index++).setCellValue("PP");
                        }else{
                            row.createCell(index++).setCellValue(sub.getTW());
                            row.createCell(index++).setCellValue(sub.add(0,sub.getTW()));
                        }
                    }else if(id == 6){
                        row.createCell(index++).setCellValue(sub.getPR());
                        row.createCell(index++).setCellValue(sub.getOR());
                        row.createCell(index++).setCellValue(sub.add(sub.getPR(),sub.getOR()));
                    }else if(id > 10){
                        id-=10;
                        subject tmpsub = student.getSemesterList().get(sm).getSubjectList().get(++position);
                        if(id == 4){
                            int tw = -1,pr = -1;
                            tw = sub.getTW();
                            pr = sub.getPR();
                            if(tw<0){
                                tw = tmpsub.getTW();
                            }else{
                                pr = tmpsub.getPR();
                            }
                            row.createCell(index++).setCellValue(tw);
                            row.createCell(index++).setCellValue(pr);
                            row.createCell(index++).setCellValue(sub.add(tw,pr));
                        }else if(id == 5){
                            int tw = -1,or = -1;
                            tw = sub.getTW();
                            or = sub.getOR();
                            if(tw<0){
                                tw = tmpsub.getTW();
                            }else{
                                or = tmpsub.getOR();
                            }
                            row.createCell(index++).setCellValue(tw);
                            row.createCell(index++).setCellValue(or);
                            row.createCell(index++).setCellValue(sub.add(tw,or));
                        }else if(id == 6){
                            int or = -1,pr = -1;
                            or = sub.getOR();
                            pr = sub.getPR();
                            if(or<0){
                                or = tmpsub.getOR();
                            }else{
                                pr = tmpsub.getPR();
                            }
                            row.createCell(index++).setCellValue(pr);
                            row.createCell(index++).setCellValue(or);
                            row.createCell(index++).setCellValue(sub.add(pr,or));
                        }
                    }
                    subjectNo++;
                    lastindex = index;
                }
            }

        }
        // Resize all columns to fit the content size
        for(int ij = 0; ij < columns.length; ij++) {
            sheet.autoSizeColumn(ij);
        }

        // Write the output to a file
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(parentDIR+"/"+filename+".xlsx");
            workbook.write(fileOut);
            fileOut.close();
            // Closing the workbook
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void printStudent() {
        for (Student student : studentArrayList) {
            plainPrint(student.toString());
            for (Semester semester : student.getSemesterList()) {
                plainPrint(semester.toString());
                for (subject sub : semester.getSubjectList()) {
                    plainPrint(sub.toString());
                }
            }
//            sc.nextInt();
        }
    }

    void printSemester() {
        for (Semester semester : semesters) {
            plainPrint(semester.toString());
            for (subject sub : semester.getSubjectList()) {
                plainPrint(sub.toString());
            }
        }
    }

    static void plainPrint(String s) {
        System.out.println(s);
    }
    static void plainPrintf(String s) {
        System.out.print(s);
    }
    static ArrayList<subject> subjectArrayList = new ArrayList<>();

    private void parseLine(String[] word, Student student) {
        String res = "";
        for (String tmp : word) {

            if (tmp.length() != 0) {
//                printf(tmp+" [[word_len:"+word.length);
                try {
                    if (tmp.contains("SEM")) {
//                        printf("yes :"+tmp.substring(tmp.length()-1));
                        int tsem = Integer.parseInt(tmp.substring(tmp.length() - 1));
                        if (tsem != sem) {
                            Semester semester = new Semester();
                            semester.setSemNo(sem);
                            ArrayList<subject> tmpList = new ArrayList<>(subjectArrayList);
                            semester.setSubjectList(tmpList);
                            if (sem == 1) {
                                semesters = new ArrayList<>();
                            }
                            semesters.add(semester);
                            subjectArrayList.clear();
                            sem = tsem;
                        }
                    } else if (word.length >= 13) {
                        subject sub = getSubject(word);
                        if (sub != null) {
                            subjectArrayList.add(sub);
                        }
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {
//                printf(tmp+" [[word_len:"+word.length);
            }
        }
    }

    private static subject getSubject(String[] word) {
//        System.out.println("test::\t");
//        printArr(word);
        subject subject = new subject(word[1]);
        subset.add(word[1]);
        int index = 0;
        if (!word[2].equalsIgnoreCase("*")) {
            index = 1;
        }
//        System.out.println(index);
        subject.setTotP(word[9 - index]);
        subject.setCrd(getMarks(word[10 - index]));
        subject.setGrd(word[11 - index]);
        subject.setGPts(getMarks(word[12 - index]));
        subject.setCPts(getMarks(word[13 - index]));
        /***
         *      Varying Part
         *      0 - IN
         *      1 - TW
         *      2 - PR
         *      3 - OR
         *      4 - TW + PR
         *      5 - TW + OR
         *      6 - PR + OR
         *
         * ****/
        if (isMarks(word[3 - index])) {
            submapper.put(word[1],0);
            subject.setIN(getMarks(word[3 - index]));
        }
        if (isMarks(word[4 - index])) {
            submapper.put(word[1],0);
            subject.setTH(getMarks(word[4 - index]));
        }
        if (isMarks(word[6 - index])) {
            subject.setTW(getMarks(word[6 - index]));
        }
        if (isMarks(word[7 - index])) {
            subject.setPR(getMarks(word[7 - index]));
        }
        if (isMarks(word[8 - index])) {
            subject.setOR(getMarks(word[8 - index]));
        }
        if(subject.getTW()>=0){
            if(subject.getPR()>=0)
                submapper.put(word[1],4);
            else if (subject.getOR()>=0)
                submapper.put(word[1],5);
            else{
                if(submapper.containsKey(word[1])){
                    int oldid = submapper.get(word[1]);
                    switch (oldid){
                        case 2: submapper.put(word[1],14);
                            break;
                        case 3: submapper.put(word[1],15);
                            break;
                    }
                }else
                    submapper.put(word[1], 1);
            }
        } else if(subject.getOR()>=0){
            if(subject.getPR()>=0)
                submapper.put(word[1],6);
            else if (subject.getTW()>=0)
                submapper.put(word[1],5);
            else{
                if(submapper.containsKey(word[1])){
//                    printf("contains the key: ",word[1]);
                    int oldid = submapper.get(word[1]);
                    switch (oldid){
                        case 1: submapper.put(word[1],15);
                            break;
                        case 2: submapper.put(word[1],16);
                            break;
                    }
                }else
                    submapper.put(word[1], 3);
            }
        } else if(subject.getPR()>=0){
            if(subject.getOR()>=0)
                submapper.put(word[1],6);
            else if (subject.getTW()>=0)
                submapper.put(word[1],4);
            else {
                if(submapper.containsKey(word[1])){
//                    printf("contains the key: ",word[1]);
                    int oldid = submapper.get(word[1]);
                    switch (oldid){
                        case 1: submapper.put(word[1],14);
                            break;
                        case 3: submapper.put(word[1],16);
                            break;
                    }
                }else
                    submapper.put(word[1], 2);
            }
        }
//        System.out.println(subject.toString());

        return subject;
    }

    static boolean isMarks(String test) {
        if (test.trim().equalsIgnoreCase("-------")) {
            return false;
        } else {
            return true;
        }
    }

    private static int getMarks(String s) {
        String tmp[] = s.split("/");
//        printf(s);
//        printf(tmp);
        try {
            return Integer.parseInt(tmp[0]);
        } catch (Exception e) {
            String tmp1[] = tmp[0].split("[$]");
//            plainPrintf(tmp[0]+"\t:\t");
//            printf(tmp1);
//            plainPrint("");
            try{
                return Integer.parseInt(tmp1[0]);
            }catch (Exception e1){
                return 00;
            }
        }
    }

    private static void printf(String... params) {
        int x = 0;
        for (String par : params) {
            System.out.print(x++ + " [" + par + "]\t");
        }
    }

    static void printArr(String[] arr) {
        int x = 0;
        printf("arrLen:\t" + arr.length);
        for (String tmp : arr) {
            if (tmp.length() == 0) {
                log(x++ + "b:\t", tmp);
            } else {
                log(x++ + "!b:\t", tmp);
            }
        }
    }

    static void log(String tag, String msg) {
        System.out.println(tag + "?" + msg);
    }

    static void printf(String msg) {
        System.out.println(msg + "\t len:" + msg.length());
    }

    static void hl() {
        printf("....................................................................................................");
    }

    static String hl2s() {
        return "....................................................................................................";
    }
}
