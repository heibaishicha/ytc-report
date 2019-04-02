package common.utils.hiveUdf;

import org.apache.hadoop.hive.ql.exec.UDF;

public class SexUDF extends UDF {

    //SexUDF函数
    public String evaluate(String sex, String name){
        try{
            if(sex.equals("man")){
                return sex + "=========>" + name;
            }
            return sex + "========>" + name;
        }catch(Exception e){
           return null;
        }
    }
}
