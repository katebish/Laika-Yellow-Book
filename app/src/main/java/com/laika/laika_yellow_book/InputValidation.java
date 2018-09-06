package com.laika.laika_yellow_book;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class InputValidation {
    private DataLine data;
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    public void setData(DataLine data) {
        this.data = data;
    }

    public String validate(String textInput, int index) {
        try {
            switch (index) {
                case 0:
                    data.cowNum = Integer.parseInt(textInput);
                    if (data.cowNum < 0) {
                        return "Cow number cannot be negative";
                    }
                    break;
                case 1:
                    if(data.dueCalveDate == null) {
                        textInput = textInput.replaceAll("/","-");
                        data.dueCalveDate = format.parse(textInput);
                    }
                    break;
                case 2:
                    data.sireOfCalf = Integer.parseInt(textInput);
                    if(data.sireOfCalf < 0) {
                        return "Sire id cannot be negative";
                    }
                    break;
                case 3:
                    data.calfBW = Double.parseDouble(textInput);
                    if (data.calfBW < 0) {
                        return "calf BW cannot be negative";
                    }
                    break;
                case 4:
                    if(data.calvingDate == null)
                        data.calvingDate = format.parse(textInput);
                    break;
                case 5:
                    if (textInput.matches("(?i)bull|male|b")) {
                        data.sex = "Bull";
                    } else if (textInput.matches("(?i)heifer|female|f")) {
                        data.sex = "Heifer";
                    } else {
                        return "Cow sex invalid";
                    }
                    break;
                case 6:
                    if(textInput.matches("(?i)reared|r|are")) {
                        data.fate = "R";
                    } else if(textInput.matches("(?i)bobbied|b|be|bee")) {
                        data.fate = "B";
                    } else if(textInput.matches("(?i)sold|moved for rearing|s|moved")) {
                        data.fate = "S";
                    } else if(textInput.matches("(?i)died|d")) {
                        data.fate = "D";
                    } else {
                        return "Invalid fate.";
                    }
                    break;
                case 7:
                    data.calfIndentNo = Integer.parseInt(textInput);
                    if (data.calfIndentNo < 0) {
                        return "calf number cannot be negative";
                    }
                    break;
                case 8:
                    data.calvingDiff = textInput;
                    break;
                case 9:
                    data.condition = textInput;
                    break;
                case 10:
                    data.remarks = textInput;
                    break;
            }
        }
        catch (NumberFormatException e) {
            return "Invalid integer, please try again.";
        }
        catch (ParseException e) {
            return "Invalid date format, please try again.";
        }
        return "";
    }

    public Date parseDate(String text) {
        //e.g. first of August 2018
        text = text.replace(" of","");
        String[] date = text.split(" ");
        switch (date[0]) {
            case "first":
                date[0] = "1";
                break;
            case "second":
                date[0] = "2";
                break;
            case "third":
                date[0] = "3";
                break;
        }
        date[0] = date[0].replace("st","").replace("nd","").replace("rd","").replace("th","");
        String finalDate = Arrays.toString(date).replaceAll(",","").replace("[","").replace("]","");
        try {
            Date parsedDate = new SimpleDateFormat("dd MMMM yyyy").parse(finalDate);
            finalDate = format.format(parsedDate);
            parsedDate = format.parse(finalDate);
            return parsedDate;
        } catch (ParseException e) {
            return null;
        }
    }

}