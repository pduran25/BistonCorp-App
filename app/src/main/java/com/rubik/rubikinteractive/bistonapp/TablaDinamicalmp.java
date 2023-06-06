package com.rubik.rubikinteractive.bistonapp;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.rubik.rubikinteractive.bistonapp.Conexion.Utilidades;

import java.util.ArrayList;

/**
 * Created by RubikInteractive on 4/26/19.
 */

public class TablaDinamicalmp {

    private TableLayout tableLayout;
    private Context context;
    private String []header;
    private ArrayList<String[]> data;
    private TableRow tableRow;
    private TextView txtCell;
    private int indexC;
    private int indexR;
    private boolean multiColor = false;
    int firstcolor, secondcolor;
    private ArrayList<CheckBox> ListCheck;


    public TablaDinamicalmp(TableLayout tableLayout, Context context) {
        this.tableLayout = tableLayout;
        this.context = context;
        ListCheck = new ArrayList<>();
    }

    public void RemoveTable(){
        this.tableLayout.removeAllViews();
    }

    public void addHeader(String []Header){
        this.header = Header;
        createHeader();
        this.data = new ArrayList<>();
    }

    public void addData(ArrayList<String[]> data){
        this.data = data;
        createDataTable();
    }

    public void addDatafija(ArrayList<String[]> data){
        this.data = data;
        createDataTable2();
    }

    private void newRow(){
        tableRow = new TableRow(context);

    }

    private void newCell(){
        txtCell = new TextView(context);
        txtCell.setGravity(Gravity.CENTER);
        txtCell.setTextSize(12);
        txtCell.setHeight(60);
    }

    public void addItem(String []item){
        String info;
        data.add(item);
        indexC = 0;
        newRow();
        while(indexC<header.length){
            newCell();
            info=(indexC<item.length)?item[indexC++]:"";
            if(info.length()>20) {
                info = info.substring(0, 20) + "...";
            }
            txtCell.setText(info);
            if(indexC == item.length) {
                EditText newsp = new EditText(context);
                newsp.setTextSize(12);
                newsp.setText(info);
                newsp.setInputType(InputType.TYPE_CLASS_NUMBER);
                Utilidades.listaEdit.add(newsp);
                tableRow.addView(newsp,newTableRowParams());
            }else
                tableRow.addView(txtCell,newTableRowParams());

        }
        tableLayout.addView(tableRow,data.size());
        reColoring();
    }


    public void addItemfijo(String []item){
        String info;
        data.add(item);
        indexC = 0;
        newRow();
        while(indexC<header.length){
            newCell();
            info=(indexC<item.length)?item[indexC++]:"";
            if(info.length()>20) {
                info = info.substring(0, 20) + "...";
            }
            txtCell.setText(info);

            tableRow.addView(txtCell,newTableRowParams());

        }
        tableLayout.addView(tableRow,data.size());
        reColoring();
    }

    public ArrayList<Integer> getSelected(){
        ArrayList<Integer> seleccionado = new ArrayList<>();
        int select = 0;
        int i = 0;
        CheckBox chsel;

        while(i< ListCheck.size()){
            chsel = ListCheck.get(i);
            if(chsel.isChecked()){
                select = chsel.getId();
                seleccionado.add(select);
            }
            i++;

        }
        return seleccionado;
    }



    private void createHeader(){
        indexC = 0;
        newRow();
        while(indexC<header.length){
            newCell();
            txtCell.setText(header[indexC++]);
            txtCell.setTextColor(Color.WHITE);
            tableRow.addView(txtCell,newTableRowParams());
        }
        tableLayout.addView(tableRow);
    }

    private TableRow.LayoutParams newTableRowParams(){
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.setMargins(1,1,1,1);
        params.weight = 1;
        return params;
    }

    private void createDataTable(){
        String info;
        for(indexR=1;indexR<=data.size();indexR++){
            newRow();
            for(indexC=0;indexC<header.length;indexC++){
                newCell();
                String [] row = data.get(indexR-1);
                info =(indexC<row.length)?row[indexC]:"";
                if(info.length()>20) {
                    info = info.substring(0, 20) + "...";
                }
                txtCell.setText(info);

                if(indexC == row.length) {
                    Spinner newsp = new Spinner(context);
                    newsp.setAdapter(Utilidades.AdpEstados);
                    Utilidades.listaSpinner.add(newsp);
                    tableRow.addView(newsp,newTableRowParams());
                }else
                    tableRow.addView(txtCell,newTableRowParams());
            }
            tableLayout.addView(tableRow);
        }
    }

    private void createDataTable2(){
        String info;
        for(indexR=1;indexR<=data.size();indexR++){
            newRow();
            for(indexC=0;indexC<header.length;indexC++){
                newCell();
                String [] row = data.get(indexR-1);
                info =(indexC<row.length)?row[indexC]:"";
                if(info.length()>20) {
                    info = info.substring(0, 20) + "...";
                }
                txtCell.setText(info);
                tableRow.addView(txtCell,newTableRowParams());
            }
            tableLayout.addView(tableRow);
        }
    }

    public void backgroundHeader(int color){
        indexC = 0;
        while(indexC<header.length){
            txtCell = getCell(0,indexC++);
            txtCell.setBackgroundColor(color);
        }
    }

    public void backgroundData(int firstcolor, int secondcolor){
       /* for(indexR=1;indexR<=data.size();indexR++){
            multiColor =! multiColor;
            for(indexC=0;indexC<header.length;indexC++){
                txtCell=getCell(indexR, indexC);
                txtCell.setBackgroundColor((multiColor)?firstcolor:secondcolor);
            }
        }*/
        this.firstcolor = firstcolor;
        this.secondcolor = secondcolor;
    }

    public void backgroundData2(int firstcolor, int secondcolor){
        for(indexR=1;indexR<=data.size();indexR++){
            multiColor =! multiColor;
            for(indexC=0;indexC<header.length;indexC++){
                txtCell=getCell(indexR, indexC);
                txtCell.setBackgroundColor((multiColor)?firstcolor:secondcolor);
            }
        }
        this.firstcolor = firstcolor;
        this.secondcolor = secondcolor;
    }


    public void reColoring(){
        indexC = 0;
        multiColor =!multiColor;
        while(indexC<header.length){
            if(indexC!=header.length-1){
                txtCell = getCell(data.size(),indexC++);
                txtCell.setBackgroundColor((multiColor)?firstcolor:secondcolor);
            }else{
                indexC++;
                //Spinner sp = getCellsp(data.size(),indexC++);
               // sp.setBackgroundColor((multiColor)?firstcolor:secondcolor);
            }

            if(!multiColor)
                txtCell.setTextColor(Color.WHITE);

        }
    }

    private TableRow getRow(int index){
        return (TableRow)tableLayout.getChildAt(index);
    }

    private TextView getCell(int rowindex, int columindex){
        tableRow = getRow(rowindex);
        return (TextView) tableRow.getChildAt(columindex);
    }

    private Spinner getCellsp(int rowindex, int columindex){
        tableRow = getRow(rowindex);
        return (Spinner) tableRow.getChildAt(columindex);
    }


}
