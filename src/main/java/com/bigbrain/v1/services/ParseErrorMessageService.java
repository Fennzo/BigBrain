package com.bigbrain.v1.services;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ParseErrorMessageService implements ParseErrorMessageInterface {

    @Override
    public String parseErrorMessage(String errorMessage) {
        String parsedMessage;
        System.out.println("EXCEPTION ERROR " + errorMessage);
        if(errorMessage.contains("constraint")){
            Pattern pattern = Pattern.compile("column\\s'(\\w+)'\\.");
            Matcher matcher = pattern.matcher(errorMessage);
            matcher.find();
            parsedMessage = matcher.group(1) + " is invalid";
        }
        else{
            Pattern pattern = Pattern.compile("(?<=error code \\[50000];\\s)([^;]*$)");
            Matcher matcher = pattern.matcher(errorMessage);
            matcher.find();
            parsedMessage = matcher.group(1);

        }
        return parsedMessage;
    }
}
