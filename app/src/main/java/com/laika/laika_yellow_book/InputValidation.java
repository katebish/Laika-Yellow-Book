package com.laika.laika_yellow_book;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
                        throw new ValidationError("Cow number cannot be negative");
                    }
                    break;
                case 1:
                    data.calfIndentNo = Integer.parseInt(textInput);
                    if (data.calfIndentNo < 0) {
                        return "calf number cannot be negative";
                    }
                    break;
                case 2:
                    if(data.dueCalveDate == null)
                        data.dueCalveDate = format.parse(textInput);
                    break;
                case 3:
                    data.sireOfCalf = Integer.parseInt(textInput);
                    break;
                case 4:
                    data.calfBW = Double.parseDouble(textInput);
                    if (data.calfBW < 0) {
                        return "calf BW cannot be negative";
                    }
                    break;
                case 5:
                    if(data.calvingDate == null)
                        data.calvingDate = format.parse(textInput);
                    break;
                case 6:
                    data.calvingDiff = textInput;
                    break;
                case 7:
                    data.condition = textInput;
                    break;
                case 8:
                    if (textInput.matches("(?i)bull|male|b")) {
                        data.sex = "Bull";
                    } else if (textInput.matches("(?i)heifer|female|f")) {
                        data.sex = "Heifer";
                    } else {
                        return "Cow sex invalid";
                    }
                    break;
                case 9:
                    data.fate = textInput;
                    break;
                case 10:
                    data.remarks = textInput;
                    break;
            }
        }
        catch (ParseException e) {
            return e.getMessage();
        }
        catch (ValidationError validationError) {
            return validationError.getMessage();
        }
        return null;
    }

    private class ValidationError extends Throwable {
        public ValidationError(String error) {
            super(error);
        }
    }
}