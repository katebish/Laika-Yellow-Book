package com.laika.laika_yellow_book;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;

public class WordsToNumber
{
    private static HashMap<String,Integer> vals = new HashMap<>();
    static {
        vals.put("naught", 0);
        vals.put("nought", 0);
        vals.put("nil", 0);
        vals.put("o", 0);
        vals.put("zero", 0);
        vals.put("one", 1);
        vals.put("two", 2);
        vals.put("three", 3);
        vals.put("four", 4);
        vals.put("five", 5);
        vals.put("six", 6);
        vals.put("seven", 7);
        vals.put("eight", 8);
        vals.put("nine", 9);
        vals.put("ten", 10);
        vals.put("eleven", 11);
        vals.put("twelve", 12);
        vals.put("thirteen", 13);
        vals.put("fourteen", 14);
        vals.put("fifteen", 15);
        vals.put("sixteen", 16);
        vals.put("seventeen", 17);
        vals.put("eighteen", 18);
        vals.put("nineteen", 19);
        vals.put("twenty", 20);
        /*
        //Currently can only read number in digit by digit basis
        //thirty two -> 302
        vals.put("thirty", 30);
        vals.put("forty", 40);
        vals.put("fifty", 50);
        vals.put("sixty", 60);
        vals.put("seventy", 70);
        vals.put("eighty", 80);
        vals.put("ninety", 90);
        vals.put("hundred", 100);
        vals.put("thousand", 1000);
        */
    }

    public static String convertNumbers(String input)
    {
        String output = input;
        String[] arr = input.split(" ");

        Integer idx      = 0;
        Integer idx_init = null;
        Integer idx_fin  = null;
        Integer t        = null;
        for(int i=0; i<arr.length; i++)
        {
            //Switch order so have 0th edge case outside loop instead of end
            while(vals.get(arr[i]) != null)
            {
                if (idx_init == null)
                {
                    idx_init = idx;
                    idx_fin = idx_init+arr[i].length();
                } else idx_fin = arr[i].length() + idx;

                idx += 1+arr[i].length();
                Object r = null;

                if(i < arr.length-1) r = vals.get(arr[i+1]);

                if(r == null)
                {
                    t = WordsToNumber.toInt(output.substring(idx_init, idx_fin));

                    output = output.substring(0, idx_init)
                            + t.toString()
                            + output.substring(idx_fin, output.length());

                    idx = idx_init + t.toString().length()+1;
                    idx_init =null;
                    idx_fin  =null;
                }
                if(i++ >= arr.length-1) break;
            }
            if(i<arr.length-1) idx += 1+arr[i].length();
        }

        return output;
    }

    public static int toInt(String input)
    {
        input = input.toLowerCase();//.replaceAll("and", "");
        String[] input_split = input.split(" ");

        return start(new ArrayList<String>(Arrays.asList(input_split)), 0);
    }

    private static int start(ArrayList<String> a, int sum)
    {
        if(a.size()==0) return sum;

        String head = a.get(0);
        //removes head from list
        a.remove(0);

        Integer t;
        if((t=vals.get(head)) != null)
        {
            if(sum==0) sum = t;
            else sum = Integer.valueOf(sum + "" + t);
        }
        else return sum;

        return start(a, sum);
    }
}
