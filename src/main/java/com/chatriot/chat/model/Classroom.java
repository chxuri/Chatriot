import java.util.List;

package com.chatriot.chat.model;

public class Classroom 
{
    private String subject;
    private String teacher;
    private String period;
    private String classId;
    private List<Integer> periods;

    /*
    public Classroom(String s, String t, int p, String c)
    {
        subject = s;
        teacher = t;
        period = p;
        classId = c;
    }
    */

    public Classroom()
    {

    }

    public String getSubject()
    {
        return subject;
    }
    public String getTeacher()
    {
        return teacher;
    }
    public String getPeriod()
    {
        return period;
    }
    public String getClassId()
    {
        return classId;
    }

    public List<Integer> getPeriods()
    {
        return periods;
    }

    public void setSubject(String s)
    {
        subject = s;
    }
    public void setTeacher(String t)
    {
        teacher = t;
    }
    public void setPeriod(String p)
    {
        period = p;
    }
    public void setClassId(String c)
    {
        classId = c;
    }

    public void setPeriods(List<Integer> p)
    {
        periods = p;
    }

}