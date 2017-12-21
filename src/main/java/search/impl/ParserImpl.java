package search.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import search.Parser;
import vo.Program;

import java.io.*;
import java.util.UUID;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserImpl implements Parser{

    public File data = new File("data.txt");
    public PrintStream psData = null;
    public int nCount = 1;
    public Program parseHtml(String html){
        try {
            psData = new PrintStream(new FileOutputStream(data,true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            //计数
            System.out.println("No."+nCount);
            psData.println("No."+nCount);
            nCount++;
            //UUID
            String uuid = UUID.randomUUID().toString();
            System.out.println("UUID: "+uuid);
            psData.println("UUID: "+uuid);
            //国家
            System.out.println("Country: Britain");
            psData.println("Country: Britain");
            //学校
            System.out.println("University: Queen Mary University of London");
            psData.println("University: Queen Mary University of London");

            Document doc = Jsoup.connect(html).get();
            Elements tabs = doc.getElementsByClass("tabs");
            Document tabsDoc = Jsoup.parse(tabs.toString());
            Elements postgrad = tabsDoc.getElementsByClass("postgrad-course");
            Document postgradDoc = Jsoup.parse(postgrad.toString());
            String information =postgradDoc.text();
            //院系
            //(?<=more: ).*(?=Tel:)学校
            Pattern school = Pattern.compile("(?<=more: ).*(?=Tel:)");
            Matcher matcherSchool = school.matcher(information);
            MatchResult School = null;
            while (matcherSchool.find()) {
                School = matcherSchool.toMatchResult();
                System.out.println("School: "+School.group());
                psData.println("School: "+School.group());
            }
            //项目名称
            Elements title = doc.getElementsByTag("title");
            Document titleDoc = Jsoup.parse(title.toString());
            String ProgramName = titleDoc.text();
            Pattern programName=Pattern.compile("^.*(?= - Queen)");
            Matcher matcherProgram = programName.matcher(ProgramName);
            MatchResult Program = null;
            while (matcherProgram.find()) {
                Program = matcherProgram.toMatchResult();
                System.out.println("ProgramName: "+Program.group());
                psData.println("ProgramName: "+Program.group());
            }

            System.out.println("Homepage: "+html);
            psData.println("Homepage: "+html);

            //(?<=email: ).*邮箱
            Pattern email = Pattern.compile("(?<=email: ).*");
            Matcher matcherEmail = email.matcher(information);
            MatchResult Email = null;
            while (matcherEmail.find()) {
                Email = matcherEmail.toMatchResult();
                System.out.println("Email: "+Email.group());
                psData.println("Email: "+Email.group());
            }

            //(?<=Tel: ).*(?=email)电话
            Pattern number = Pattern.compile("(?<=Tel: ).*(?=email)");
            Matcher matcherNumber = number.matcher(information);
            MatchResult Number = null;
            while (matcherNumber.find()) {
                Number = matcherNumber.toMatchResult();
                System.out.println("PhoneNumber: "+Number.group());
                psData.println("PhoneNumber: "+Number.group());
            }

            System.out.println("=====================================");
            psData.println("=====================================");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
