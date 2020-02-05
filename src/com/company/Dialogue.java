package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dialogue {

    String name;
    List<String> body;

    void setBody(String bodyString) {
        body = Arrays.asList(bodyString.split("\n"));

    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        for (String line : body) {
            out.append(line);
        }
        return out.toString();
    }

    boolean isType(int lineNumber, char type) {
        if (getLine(lineNumber).length() >= 1) {
            return getLine(lineNumber).charAt(0) == type;
        }
        return false;
    }

    String getLine(int lineNumber) {
        if (lineNumber >= body.size() || lineNumber < 0) {
            return "";
        }
        return body.get(lineNumber);

    }

    Dialogue(String name) {
        body = new ArrayList<>();
        this.name = name;
    }
}
