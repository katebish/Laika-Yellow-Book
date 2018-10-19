package com.laika.laika_yellow_book;

import java.util.Arrays;
import java.util.stream.Stream;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {


    private static final String[] regex_keys = new String[]{
            "cow( number| id)?",
            "calf( number| id)?",
            "(calf )?(due( date)?|expected( to be( born)?)?)( on)?",
            "(sire|bull)( number| id)?( of calf)?",
            "(calf )?(bw|birth weight|weighs)",
            "(cal(f|ving)?|actual(ly)?) (date|(born( on)?))",
            "difficulty",
            "condition",
            "gender|sex",
            "fate",
            "(remark|comment)s?"
    };



    private static final String[] regex_vals = new String[]{
            "\\d+",
            "\\d+",
            ".*",
            "\\d+",
            "\\d+( ?(point|dot|\\.) ?\\d+)?(kg|kilo|lb|pounds)?",
            ".*",
            "[a-zA-Z]+",
            "[a-zA-Z]+",
            "[hb](\\S)*",
            "[br](\\S)*",
            ".*"
    };



    private static final String[] table_keys = new String[]{
            "cow number", "calf number", "calf due date", "sire", "bw",
            "calving date", "difficulty", "condition", "sex", "fate", "remarks"
    };


    public static HashMap<String,String> parse(String input) {

        HashMap<String,String> vals = new HashMap<>();
        String[] regex_concat = new String[regex_keys.length];
        for(int i=0; i<regex_keys.length; i++) {
             regex_concat[i] = '('+regex_keys[i]+' '+regex_vals[i]+')';
        }
        //create array of partial patterns

        /* lambdas aren't supported with sdk/jdk version

        Pattern[] ps = Arrays.stream(regex_concat)

                .map(k -> Pattern.compile(k))

                .toArray(Pattern[]::new);

        */
        Pattern[] ps = new Pattern[regex_concat.length];
        for (int i=0; i<regex_concat.length; i++)
            ps[i] = Pattern.compile(regex_concat[i]);

        //Monolithic regex
        StringBuilder regExp = new StringBuilder();
        regExp.append(regex_concat[0]);
        for(String s : regex_concat) {

            if(s == regex_concat[0]) continue;
            regExp.append('|');
            regExp.append(s);
        }
        String regex = regExp.toString();
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(input.toLowerCase());

        while(m.find()) {
            String group = m.group();
            String[] group_split = {"",""};
            //if string matches partial pattern, set key to std format
            //  eg (cow (id|number)) -> cow number
            for(int i=0; i<regex_keys.length; i++) {
                if(ps[i].matcher(group).matches()) {
                    group_split[0] = table_keys[i];
                    //remove matching key and leading space
                    group_split[1] = group.replaceFirst(regex_keys[i]+' ', "");
                    //If the key is a date (captures all subsequent text),
                    //    reparse the string after the key
                    if(group_split[0].matches(table_keys[2]+"|"+table_keys[5]))
                        vals.putAll(Parser.parse(group_split[1]));
                    //Finds beging of match in group_split[1] if it exists
                    //    then removes all after that point
                    Matcher m2 = p.matcher(group_split[1]);
                    if(m2.find()) group_split[1] = group_split[1].substring(0, m2.start());
                    //OLD. kept stray words but worked otherwise
                    //group_split[1] =group_split[1].replaceAll(regex, "");
                }
            }
            vals.put(group_split[0], group_split[1]);
        }
        return vals;
    }

}