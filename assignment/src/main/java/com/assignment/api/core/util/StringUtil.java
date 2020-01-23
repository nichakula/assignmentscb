package com.assignment.api.core.util;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.text.WordUtils;

public class StringUtil {

    public static final String DEFAULT_CURRENCY_FORMAT = "#,##0.00";
    public static final String SIMPLE_DOUBLE = "0.00";
    public static final String INTEGER_FORMAT = "#,##0";
    public static final String BLANK = "";
    public static final String COMMA_SINGLE_QUOTE = ",'";
    public static final String COMMA = ",";
    public static final String SEMICOLON = ";";
    public static final String SINGLE_QUOTE = "'";
    public static final String TWO_SINGLE_QUOTE = "''";
    public static final String SHARP = "#";
    public static final String PIPE = "|";

    public static String getBatchTypeDesc(String type) {
        String desc = "";
        if ("T".equals(type)) {
            desc = "Tele Pro";
        } else if ("C".equals(type)) {
            desc = "CRM";
        }
        return desc;
    }

    interface OS {

        String Windows = "windows";
        String Linux = "linux";
        String Mac = "linux";
        String Sun = "sunos";
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isEmpty(Object val) {
        if (val == null) {
            return true;
        }
        if (val instanceof String) {
            return isBlank((String) val);
        }

        return false;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static String formatCurrentcy(String format, BigDecimal currency) {
        NumberFormat df = new DecimalFormat(format);
        String formatStr = df.format(currency);
        return formatStr;
    }

    public static String formatCurrentcy(BigDecimal currency) {
        if (currency == null) {
            return "";
        }
        return formatCurrentcy(DEFAULT_CURRENCY_FORMAT, currency);
    }

    public static int removeNumberCommas(String numberWithComma) {
        if (isEmpty(numberWithComma)) {
            return 0;
        }
        if (numberWithComma != null && numberWithComma.indexOf(',') > 0) {
            return Integer.parseInt(numberWithComma.replace(",", ""));
        }
        return Integer.parseInt(numberWithComma.trim());
    }

    public static String notNull(String testStr) {
        return testStr == null ? BLANK : testStr;
    }

    /**
     * *
     * if null or blank return null else return value.trim *
     */
    public static String trimEmptyToNull(String testStr) {
        return isBlank(testStr) ? null : testStr.trim();
    }

    /**
     * *
     * if null return "" if have a value return that value.trim *
     */
    public static String trimNotNull(String testStr) {
        return isEmpty(testStr) ? notNull(testStr).trim() : testStr.trim();
    }

    public static String emptyToNull(String testStr) {
        return isBlank(testStr) ? null : testStr;
    }

    public static String toString(Object obj) {
        return ToStringBuilder.reflectionToString(obj);
    }

    public static String removeStartWithZero(String text) {

        String result = text.replaceFirst("^0*", "");
        System.out.println("result: " + result);
        return result;
    }

    /**
     * *
     * Replace /xxx/ with replace string
     *
     * @param text ex., text= "select /a,b,c/ from orders ";
     * @param replaceWith ex., "1"
     * @return newSql = "select 1 from orders" if match else just return orginal
     * text sql
     */
    public static final String REGEX = "(/.+?/)";

    public static String replaceSql(String text, String replaceWith) {
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(text);
        boolean found = m.find();
        if (found) {
            return m.replaceAll(replaceWith);
        } else {
            return text;
        }
    }

    public static void main(String[] args) {
    }

    /**
     * **
     * Support only Oracle data type for right now
     *
     *
     */
    private static String convertDBType2JavaType(String dbDataType) {
//        System.out.println("type : " +dbDataType);
        if (isEmpty(dbDataType)) {
            throw new IllegalArgumentException("Invalide dbDataType parameter : " + dbDataType);
        }

        if (dbDataType.startsWith("VARCHAR") || dbDataType.startsWith("VARCHAR2") || dbDataType.startsWith("CHAR")) {
            return "String";
        } else if (dbDataType.startsWith("DATE") || dbDataType.startsWith("TIMESTAMP")) {
            return "Date";
        } else if (dbDataType.startsWith("NUMBER")) {
            //that can be ex, NUMBER(12,2) or NUMBER(12,0)
            if (StringUtils.contains(dbDataType, ",")) {

                int openIdx = StringUtils.indexOf(dbDataType, "(");
                int closeIdx = StringUtils.indexOf(dbDataType, ")");
                String numVal = StringUtils.substring(dbDataType, openIdx + 1, closeIdx);
//                System.out.println("numval: " + numVal);
                //vals[0] = number size
                //vals[1] = precision size
                String[] vals = StringUtils.splitByWholeSeparator(numVal, ",");
//                System.out.println("val[0]: "+vals[0] +", val[1]:" +vals[1]);
                if (Integer.parseInt(vals[1]) > 0) {
                    return "BigDecimal";
                } else if (Integer.parseInt(vals[0]) > 10) {
                    return "Long";
                } else {
                    return "Int";
                }
                // don't have comma , so can be Int or Long, ex., NUMBER(1) , NUMBER(12)
            } else {
//                System.out.println("dbDataType : "+ dbDataType);
                if (!StringUtils.contains(dbDataType, "(")) {
                    return "Int";
                }
                int openIdx = StringUtils.indexOf(dbDataType, "(");
                int closeIdx = StringUtils.indexOf(dbDataType, ")");
                String numVal = StringUtils.substring(dbDataType, openIdx + 1, closeIdx);
                if (Integer.parseInt(numVal) > 10) {
                    return "Long";
                } else {
                    return "Int ";
                }
            }

        }

        return null;
    }

    private static String convertToPrimitive(String objectType) {
        if ("BigDecimal".equals(objectType)) {
            return "BigDecimal";
        } else if ("Long".equals(objectType)) {
            return "long";
        } else if ("Int".equals(objectType)) {
            return "int";
        } else {
            return objectType;
        }
    }

    /**
     * *
     * Display for ex,. ACTION_TYPE_ID -> actionTypeId, hello_world ->
     * helloWorld
     */
    private static void printColumnNameCase() {
        try {
            List allLine = FileUtils.readLines(new File("C:/Users/Lenovo/Documents/tableColumn.txt"));
            for (int i = 0; i < allLine.size(); i++) {
                String line = (String) allLine.get(i);
                String[] words = StringUtils.splitByWholeSeparator(line, "\t");//{"OTHER_ID", "VARCHAR2(20 BYTE)"}
                String rawCap = WordUtils.capitalizeFully(words[0], new char[]{'_'});
                String result = StringUtils.replace(rawCap, "_", "");
                result = StringUtils.uncapitalize(result);
                System.out.println("private " + convertToPrimitive(convertDBType2JavaType(words[1])) + " " + result + ";");
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        }
    }

    /**
     * *
     * Gen prepare statement ex., Column name APP_ID -> ps.setString(1,
     * customer.getAppId());
     */
    private static void printGenPrepareStatement() {
        try {
            List allLine = FileUtils.readLines(new File("D:/TableColumns.txt"));
            for (int i = 0; i < allLine.size(); i++) {
                String line = (String) allLine.get(i);
                String[] words = StringUtils.splitByWholeSeparator(line, "\t");//{"OTHER_ID", "VARCHAR2(20 BYTE)"}
                String rawCap = WordUtils.capitalizeFully(words[0], new char[]{'_'});
                String columnName = StringUtils.replace(rawCap, "_", "");
//                result = StringUtils.uncapitalize(result);
                String dataType = words[1];
                if (dataType.startsWith("DATE")) {
                    System.out.println("ps.set" + convertDBType2JavaType(words[1]) + "(i++, DateUtil.toSqlDate(item.get" + columnName + "()), Calendar.getInstance(Locale.US)); ");
                } else {
                    System.out.println("ps.set" + convertDBType2JavaType(words[1]) + "(i++, item.get" + columnName + "()); ");
                }

            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        }
    }

    private static void printGenColumnWithComma() {
        try {
            List allLine = FileUtils.readLines(new File("C:/TableColumns.txt"));
            for (int i = 0; i < allLine.size(); i++) {
                String line = (String) allLine.get(i);
                String[] words = StringUtils.splitByWholeSeparator(line, "\t");//{"OTHER_ID", "VARCHAR2(20 BYTE)"}
                String rawCap = WordUtils.capitalizeFully(words[0], new char[]{'_'});
                String columnName = StringUtils.replace(rawCap, "_", "");
//                result = StringUtils.uncapitalize(result);
                String dataType = words[1];
                String field = words[0];

                System.out.print(field + ", ");

            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        }
    }

    private static void printGenMapColumn() {
        try {
            List allLine = FileUtils.readLines(new File("C:/TableColumns.txt"));
            for (int i = 0; i < allLine.size(); i++) {
                String line = (String) allLine.get(i);
                String[] words = StringUtils.splitByWholeSeparator(line, "\t");//{"OTHER_ID", "VARCHAR2(20 BYTE)"}
                String rawCap = WordUtils.capitalizeFully(words[0], new char[]{'_'});
                String columnName = StringUtils.replace(rawCap, "_", "");
//                result = StringUtils.uncapitalize(result);
                String dataType = words[1];
                String field = words[0];

                System.out.println("sql.append(\" ,:" + field + "\");");

            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        }
    }

    private static void printGenPutStatement() {
        try {
            List allLine = FileUtils.readLines(new File("C:/TableColumns.txt"));
            for (int i = 0; i < allLine.size(); i++) {
                String line = (String) allLine.get(i);
                String[] words = StringUtils.splitByWholeSeparator(line, "\t");//{"OTHER_ID", "VARCHAR2(20 BYTE)"}
                String rawCap = WordUtils.capitalizeFully(words[0], new char[]{'_'});
                String columnName = StringUtils.replace(rawCap, "_", "");
//                result = StringUtils.uncapitalize(result);
                String dataType = words[1];
                if (dataType.startsWith("DATE")) {
                    System.out.println("param.put(\"" + words[0] + "\", DateUtil.toSqlDate(collateral.get" + columnName + "()), Calendar.getInstance(Locale.US)); ");
                } else {
                    System.out.println("param.put(\"" + words[0] + "\", address.get" + columnName + "()); ");
                }

            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        }
    }

    /**
     * *
     * Gen prepare statement ex., Column name APP_ID ->
     * item.setAppId(rs.getString("APP_ID"));
     */
    //Becase at Excise project, Database is set as A.D. but set locale as Thai that conflict itself
    public static boolean isExcisePrj = true; //just maker to identify is Excise project

    private static void printGetResultSetToVo() {
        try {
            List allLine = FileUtils.readLines(new File("C:/TableColumns.txt"));
            for (int i = 0; i < allLine.size(); i++) {
                String line = (String) allLine.get(i);
                String[] words = StringUtils.splitByWholeSeparator(line, "\t");//{"OTHER_ID", "VARCHAR2(20 BYTE)"}
                String rawCap = WordUtils.capitalizeFully(words[0], new char[]{'_'});
                String columnName = StringUtils.replace(rawCap, "_", "");
//                result = StringUtils.uncapitalize(result);
                String dbType = convertDBType2JavaType(words[1]);
                if (isExcisePrj && "Date".equalsIgnoreCase(dbType)) {
                    //System.out.println("item.set"+columnName+"(DateUtil.toDate(rs.get"+dbType+"(\""+words[0]+"\", Calendar.getInstance(Locale.US))));");  //print -> item.setAppId(rs.getString("APP_ID"));
                    System.out.println("param.put(\"" + words[0] + "\", collateralStatus.get" + columnName + "());");
                } else {
                    //System.out.println("item.set"+columnName+"(rs.get"+dbType+"(\""+words[0]+"\"));");  //print -> item.setAppId(rs.getString("APP_ID"));
                    System.out.println("param.put(\"" + words[0] + "\", collateralStatus.get" + columnName + "());");
                }

            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        }
    }

    private static void printTableNameList() {
        try {
            List allLine = FileUtils.readLines(new File("C:/TableColumns.txt"));
            for (int i = 0; i < allLine.size(); i++) {
                String line = (String) allLine.get(i);
                String rawCap = WordUtils.capitalizeFully(line, new char[]{'_'});
                String result = StringUtils.replace(rawCap, "_", "");
                System.out.println(result);
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        }
    }

    public static boolean isValidCitizenId(String inputCitizenId) {

        String stringCitizenId = new String(inputCitizenId);
        System.out.println("stringCitizenId :: " + stringCitizenId);
        if (stringCitizenId == null || stringCitizenId.length() == 0
                || stringCitizenId == "") {
            return false;
        }

        int[] i_mul = new int[]{
            13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
        String gid = stringCitizenId;
        int l_strlen = stringCitizenId.length();
        int i_weight, i_dummy, r_chkmod, s_dummy, i_lastdigit;
        int i_sum = 0;

        /*
         * if (isNaN(gid)) { return false;
        }
         */
        System.out.println("pass :step--->1");
        if (l_strlen == 0) {
            return false;
        }
        if (l_strlen != 13) {
            return false;
        }
        System.out.println("pass :step--->2");

        s_dummy = gid.charAt(12) - '0';
        System.out.println("last digit :: " + s_dummy);
        i_lastdigit = s_dummy;
        for (int i = 0; i <= 11; i++) {
            s_dummy = gid.charAt(i) - '0';
            i_dummy = s_dummy;
            i_weight = i_dummy * i_mul[i];
            i_sum = i_sum + i_weight;
        }

        i_sum = i_sum % 11;
        r_chkmod = i_sum;

        int i_chkdigit;
        if (r_chkmod == 0) {
            i_chkdigit = 1;
        } else if (r_chkmod == 1) {
            i_chkdigit = 0;
        } else {
            i_chkdigit = 11 - r_chkmod;
        }
        System.out.println("cal :: " + i_chkdigit);
        System.out.println("pass :step--->3");
        if (i_chkdigit == i_lastdigit) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidEmail(String email) {

        //Set the email pattern string
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");

        //Match the given string with the pattern
        Matcher m = p.matcher(email);

        //check whether match is found
        boolean matchFound = m.matches();

        return matchFound;
    }

    public static String Unicode2ASCII(String unicode) {
        StringBuilder ascii = new StringBuilder(unicode);
        int code;
        for (int i = 0; i < unicode.length(); i++) {
            code = (int) unicode.charAt(i);
            if ((0xE01 <= code) && (code <= 0xE5B)) {
                ascii.setCharAt(i, (char) (code - 0xD60));
            }
        }
        return ascii.toString();
    }

    public static String ASCII2Unicode(String ascii) {
        StringBuilder unicode = new StringBuilder(ascii);
        int code;
        for (int i = 0; i < ascii.length(); i++) {
            code = (int) ascii.charAt(i);
            if ((0xA1 <= code) && (code <= 0xFB)) {
                unicode.setCharAt(i, (char) (code + 0xD60));
            }
        }
        return unicode.toString();
    }

    /**
     * *
     *
     * @param List of Id if listOfId = [a,b,c] convert to => 'a','b','c' if
     * listOfId = null convert to => ''
     * @return
     */
    public static String genSqlInStr(List listOfId) {
        StringBuilder allKey = new StringBuilder();
        for (int i = 0; i < listOfId.size(); i++) {
            if (i == 0) {
                allKey.append(SINGLE_QUOTE).append(listOfId.get(i)).append(SINGLE_QUOTE);
            } else {
                allKey.append(COMMA_SINGLE_QUOTE).append(listOfId.get(i)).append(SINGLE_QUOTE);
            }
        }
        if (isEmpty(allKey.toString())) {
            allKey.append(TWO_SINGLE_QUOTE); //Set empty in sql as ''
        }
        return allKey.toString();
    }

    public static String Left(String text, int length) {
        if (text != null) {
            return text.substring(0, text.length() >= length ? length : text.length());
        } else {
            return null;
        }
    }

    public static String Right(String text, int length) {
        if (text != null) {
            return text.substring(text.length() - (text.length() >= length ? length : text.length()), text.length());
        } else {
            return null;
        }

    }

    public static String Mid(String text, int start, int end) {
        if (text != null) {
            return text.substring(start, end);
        } else {
            return null;
        }

    }

    public static String Mid(String text, int start) {
        if (text != null) {
            return text.substring(start, text.length() - start);
        } else {
            return null;
        }

    }

    public static String subString(String text, int length) {
        return subString(text, 0, length);
    }

    /**
     * *
     *
     * @param text string text
     * @param indexStart start with 0
     * @param length length of string will be sub
     * @return
     */
    public static String subString(String text, int indexStart, int length) {
        return text.substring(indexStart, indexStart + length);
    }

    public static String getFlagText(int flagId) {
        if (flagId == -1) {
            return "";
        }
        return flagId == 1 ? "ผ่าน" : "ไม่ผ่าน";
    }

    public static String getFlagValid(int flagId) {
        if (flagId == -1) {
            return "";
        }
        return flagId == 1 ? "ถูกต้อง" : "ไม่ถูกต้อง";
    }

    public static String getFlagPreemptionText(int flagId) {
        return flagId == 1 ? "ใบจอง" : "ไม่ใช่ใบจอง";
    }

    public static String getAmtTypeText(String amtType) {
        if (isEmpty(amtType)) {
            return "";
        }

        String text = "";
        if ("I".equals(amtType)) {
            text = "ในประเทศ";
        } else if ("O".equals(amtType)) {
            text = "ใบขนสินค้า";
        }
        return text;
    }

    public static String genExcelFileName(String prefix, String batchRefNo) {
        String batchReplace = batchRefNo.replace("/", "_");
        String excelFileName = prefix + batchReplace + ".xlsx";
        return excelFileName;
    }

    public static String genAuditKtbExcelFile(String batchRefNo) {
        String batchReplace = batchRefNo.replace("/", "_");
        String excelFileName = "ktb_audit_file_" + batchReplace + ".xlsx";
        return excelFileName;
    }

    public static String genReconcileExcelFileName(int recId, String batchRefNo) {
        String batchReplace = batchRefNo.replace("/", "_");
        return "reconcile_" + recId + "_" + batchReplace + ".xlsx";
    }

    /**
     * *
     * flagStr : flag number as string
     *
     * @return -1 if flagStr is null else return parseInt of flagStr
     */
    public static int getFlagValue(String flagStr) {
        return isEmpty(flagStr) ? -1 : Integer.parseInt(flagStr);
    }

//    public static String checkOS(){
//        String os = "";
//        if (System.getProperty("os.name").toLowerCase().indexOf("win") > -1) {
//            os = "windows";
//        } else if (System.getProperty("os.name").toLowerCase().indexOf("nux") > -1) {
//            os = "linux";
//        } else if (System.getProperty("os.name").toLowerCase().indexOf("mac") > -1) {
//            os = "mac";
//        } else if (System.getProperty("os.name").toLowerCase().indexOf("nix") > -1) {
//            os = "unix";
//        } else if (System.getProperty("os.name").toLowerCase().indexOf("sunos") > -1) {
//            os = "sun";
//        }
//        return os;
//    }
    public enum OSType {
        Windows, MacOS, Unix, Other
    };

    protected static OSType detectedOS;

    public static OSType checkOS() {
        if (detectedOS == null) {
            String OS = System.getProperty("os.name", "generic").toLowerCase();
            if (OS.indexOf("win") >= 0) {
                detectedOS = OSType.Windows;
            } else if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0) {
                detectedOS = OSType.Unix;
            } else if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
                detectedOS = OSType.MacOS;
            } else {
                detectedOS = OSType.Other;
            }
        }
        return detectedOS;
    }

    private static void printUpdateColumn() {
        String q = "select column_name from all_tab_columns WHERE table_name='CO_COLLATERALS'";
        try {
            StringBuilder sql = new StringBuilder();
            List allLine = FileUtils.readLines(new File("C:/TableColumns.txt"));
              for (int i = 0; i < allLine.size(); i++) {
                String line = (String) allLine.get(i);
                String[] words = StringUtils.splitByWholeSeparator(line, "\t");//{"OTHER_ID", "VARCHAR2(20 BYTE)"}
                String rawCap = WordUtils.capitalizeFully(words[0], new char[]{'_'});
                String columnName = StringUtils.replace(rawCap, "_", ""); 
                sql.append("if (obj.get" + columnName + "() != null) {\n");
                sql.append("param.put(\"" + words[0] + "\", obj.get" + columnName + "());\n");
                sql.append("sql.append(\" ," + words[0] + " = :" + words[0] + "\");\n");
                sql.append(" }\n");
            
                }System.out.println(sql.toString());
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        }
    }
    
    public static String concat(String...vals) {
      if(vals == null) {
          return null;
      }
      StringBuilder sb = new StringBuilder();
      for(String v: vals) {
          sb.append(v);
      }
      return sb.toString();
  }

}