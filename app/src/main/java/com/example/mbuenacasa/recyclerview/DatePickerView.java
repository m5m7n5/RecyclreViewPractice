package com.example.mbuenacasa.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mbuenacasa on 14/07/16.
 */
public class DatePickerView extends RelativeLayout implements AbstractGradientRecyclerView.AbstractGradientRecyclerCommunicator{

    final int monthsPerYear = 12;
    private LayoutInflater inflater;
    private SimpleStringRecyclerView days;
    private SimpleStringRecyclerView months;
    private SimpleStringRecyclerView years;
    private List<String> yearsData;
    private List<String> indexes;
    private Map<String,ArrayList<String>> daysInMonthMap;
    private Map<String,ArrayList<String>> monthsDataMap;
    private Map<String,ArrayList<String>> daysDataMap;


    public DatePickerView(Context context) {

        super(context);
        inflater = LayoutInflater.from(context);
        init();

    }

    public DatePickerView(Context context, AttributeSet attrs) {

        super(context, attrs);
        inflater = LayoutInflater.from(context);
        init();

    }

    public DatePickerView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);
        init();

    }

    private void init(){

        inflater.inflate(R.layout.view_date_picker,this,true);
        days = (SimpleStringRecyclerView) findViewById(R.id.date_picker_days_recycler);
        months = (SimpleStringRecyclerView) findViewById(R.id.date_picker_months_recycler);
        years = (SimpleStringRecyclerView) findViewById(R.id.date_picker_years_recycler);

        List<String> monthsStrings =  new ArrayList<>();

        //TODO Quitar hardcoded strings
        monthsStrings.add("JAN");
        monthsStrings.add("FEB");
        monthsStrings.add("MAR");
        monthsStrings.add("APR");
        monthsStrings.add("MAY");
        monthsStrings.add("JUN");
        monthsStrings.add("JUL");
        monthsStrings.add("AUG");
        monthsStrings.add("SEP");
        monthsStrings.add("OCT");
        monthsStrings.add("NOV");
        monthsStrings.add("DEC");

        Date start = new Date(2016, 6, 10);
        Date end = new Date(2017, 4, 9);
        generateTablesAndValues(monthsStrings,start,end);

        days.initAdapter(daysDataMap.get(indexes.get(start.getMonth())+Integer.toString(start.getYear())));
        months.initAdapter(monthsDataMap.get(Integer.toString(start.getYear())));
        years.initAdapter(yearsData);

        days.setCommunicator(this);
        months.setCommunicator(this);
        years.setCommunicator(this);

    }

    private void generateTablesAndValues(List<String> monthsStrings,Date startDate, Date endDate) {

        int startYear = startDate.getYear();
        int startMonth = startDate.getMonth();
        int startDay = startDate.getDate();
        int endYear = endDate.getYear();
        int endMonth = endDate.getMonth();
        int endDay = endDate.getDate();

        int quantityOfMonths = 12*(endYear-startYear)+endMonth-startMonth;

        List<Integer> monthsDays = new ArrayList<>();

        //Position of the days quantity in the ArrayList monthQuantity
        monthsDays.add(3);
        monthsDays.add(0);
        monthsDays.add(3);
        monthsDays.add(2);
        monthsDays.add(3);
        monthsDays.add(2);
        monthsDays.add(3);
        monthsDays.add(3);
        monthsDays.add(2);
        monthsDays.add(3);
        monthsDays.add(2);
        monthsDays.add(3);

        Map<String,Integer> monthsDayIndexMap = new LinkedHashMap<>();
        for(int i = 0;i<monthsStrings.size();i++){
            monthsDayIndexMap.put(monthsStrings.get(i),monthsDays.get(i));
        }
        indexes = new ArrayList<>(monthsDayIndexMap.keySet());


        ArrayList<Integer> monthQuantity = new ArrayList<>();
        monthQuantity.add(28);
        monthQuantity.add(29);
        monthQuantity.add(30);
        monthQuantity.add(31);

        ArrayList<ArrayList<String>> aux = new ArrayList<>();
        for(int i=0;i<monthQuantity.size();i++){
            ArrayList<String> aux2 = new ArrayList<>();
            for(int j=1;j<=monthQuantity.get(i);j++){
                aux2.add(Integer.toString(j));
            }
            aux.add(aux2);
        }

        daysInMonthMap = new HashMap<>();
        for(String s:monthsDayIndexMap.keySet()) {
            daysInMonthMap.put(s,aux.get(monthsDayIndexMap.get(s)));
        }


        daysDataMap =  new HashMap<>();
        monthsDataMap = new HashMap<>();

        if(quantityOfMonths==0){
            //Genero la lista de dias para el mes y año correspondiente
            ArrayList<String> aux2 = new ArrayList<>();
            for(int i=startDay;i<=endDay;i++){
                aux2.add(Integer.toString(i));
            }
            //Añado la lista de dias al mes y año correspondiente
            daysDataMap.put(indexes.get(startMonth)+Integer.toString(startYear),aux2);
            //Genero la lista de meses para el año
            aux2=new ArrayList<>();
            aux2.add(indexes.get(startMonth));
            //Añado la lista de meses
            monthsDataMap.put(Integer.toString(startYear),aux2);
        }else if((endYear-startYear)==0){
            //Genero los dias del mes inicial
            ArrayList<String> aux2 = new ArrayList<>();
            for(int i=startDay;i<=monthQuantity.get(monthsDays.get(startMonth));i++){
                aux2.add(Integer.toString(i));
            }
            //Añado los dias del mes inicial en funcion del mes y el año
            daysDataMap.put(indexes.get(startMonth)+Integer.toString(startYear),aux2);

            //Voy añadiendo el resto de meses completos
            for(int i=(startMonth+1);i<endMonth;i++){
                daysDataMap.put(indexes.get(i)+Integer.toString(startYear),daysInMonthMap.get(indexes.get(i)));
            }
            //Genero los dias del mes final
            aux2 = new ArrayList<>();
            for(int i=1;i<=endDay;i++){
                aux2.add(Integer.toString(i));
            }
            //Añado los dias del mes final en funcion del mes y el año.
            daysDataMap.put(indexes.get(endMonth)+Integer.toString(startYear),aux2);
            //Genero los meses del año correspondiente.
            aux2 =  new ArrayList<>();
            for(int i=startMonth;i<=endMonth;i++){
                aux2.add(indexes.get(i));
            }
            //Añado la lista de los meses correspondientes al año.
            monthsDataMap.put(Integer.toString(startYear),aux2);
        }else{
            //Generlo la lista de dias del primer mes.
            ArrayList<String> aux2 = new ArrayList<>();
            for(int i=startDay;i<=monthQuantity.get(monthsDays.get(startMonth));i++){
                aux2.add(Integer.toString(i));
            }
            //Añado la lista de dias del primer mes al mes y año correspondientes.
            daysDataMap.put(indexes.get(startMonth)+Integer.toString(startYear),aux2);
            //Añado la lista de dias de los meses siguientes hasta terminar el año.
            for(int i=(startMonth+1);i<monthsPerYear;i++){
                daysDataMap.put(indexes.get(i)+Integer.toString(startYear),daysInMonthMap.get(indexes.get(i)));
            }
            // i recorre los años y j recorre los meses.
            for(int i=(startYear+1);i<endYear;i++){
                for(int j=0;j<monthsPerYear;j++){
                    daysDataMap.put(indexes.get(j)+Integer.toString(i),daysInMonthMap.get(indexes.get(j)));
                }
            }
            //Añado los meses que faltan exceptuando el ultimo ya que puede tener una cantidad de dias menor.
            for(int i=0;i<endMonth;i++){
                daysDataMap.put(indexes.get(i)+Integer.toString(endYear),daysInMonthMap.get(indexes.get(i)));
            }
            //Añado el último mes
            aux2 = new ArrayList<>();
            for(int i=1;i<=endDay;i++){
                aux2.add(Integer.toString(i));
            }
            daysDataMap.put(indexes.get(endMonth)+Integer.toString(endYear),aux2);
            //Genero los meses para todos los años excepto el inicial y el final
            aux2 = new ArrayList<>();
            for(int i=1;i<=endDay;i++){
                aux2.add(indexes.get(i));
            }
            for(int i=(startYear+1);i<endYear;i++){
                monthsDataMap.put(Integer.toString(i),aux2);
            }
            //Genero los meses del año inicial
            aux2 =  new ArrayList<>();
            for(int i=startMonth;i<monthsPerYear;i++){
                aux2.add(indexes.get(i));
            }
            monthsDataMap.put(Integer.toString(startYear),aux2);
            //Genero los meses del año final
            aux2 =  new ArrayList<>();
            for(int i=0;i<=endMonth;i++){
                aux2.add(indexes.get(i));
            }
            monthsDataMap.put(Integer.toString(endYear),aux2);

        }

        yearsData = new ArrayList<>();
        for(int i=startYear;i<=endYear;i++){
            yearsData.add(Integer.toString(i));
        }
        //TODO comprobar si es bisiesto y ver en febrero
    }

    @Override
    public void whenSelected(AbstractGradientRecyclerView aRecycler, View view, int index){

        ((AbstractGradientRecyclerView.DynamicPosibleScrollLinearLayoutManager)days.getLayoutManager()).setCanScroll(true);
        ((AbstractGradientRecyclerView.DynamicPosibleScrollLinearLayoutManager)months.getLayoutManager()).setCanScroll(true);
        ((AbstractGradientRecyclerView.DynamicPosibleScrollLinearLayoutManager)years.getLayoutManager()).setCanScroll(true);
        if(days==aRecycler){
        }else if(months==aRecycler){

            days.resetAdapter(daysDataMap.get(months.getSelectedString()+years.getSelectedString()));
            days.getAdapter().notifyDataSetChanged();

        }else if(years==aRecycler){

            months.resetAdapter(monthsDataMap.get(years.getSelectedString()));
            months.getAdapter().notifyDataSetChanged();

            days.resetAdapter(daysDataMap.get(monthsDataMap.get(years.getSelectedString()).get(0)+years.getSelectedString()));
            days.getAdapter().notifyDataSetChanged();
        }

    }

    @Override
    public void whenScrolled(AbstractGradientRecyclerView recyclerView) {
        if(days==recyclerView){
            ((AbstractGradientRecyclerView.DynamicPosibleScrollLinearLayoutManager)months.getLayoutManager()).setCanScroll(false);
            ((AbstractGradientRecyclerView.DynamicPosibleScrollLinearLayoutManager)years.getLayoutManager()).setCanScroll(false);
        }else if(months==recyclerView){
            ((AbstractGradientRecyclerView.DynamicPosibleScrollLinearLayoutManager)days.getLayoutManager()).setCanScroll(false);
            ((AbstractGradientRecyclerView.DynamicPosibleScrollLinearLayoutManager)years.getLayoutManager()).setCanScroll(false);
        }else if(years==recyclerView){
            ((AbstractGradientRecyclerView.DynamicPosibleScrollLinearLayoutManager)days.getLayoutManager()).setCanScroll(false);
            ((AbstractGradientRecyclerView.DynamicPosibleScrollLinearLayoutManager)months.getLayoutManager()).setCanScroll(false);
        }
    }

    public String getDateAsFormatedString(){
        return days.getSelectedString()+"/"+Integer.toString(indexes.indexOf(months.getSelectedString())+1)+"/"+years.getSelectedString();
    }

}
