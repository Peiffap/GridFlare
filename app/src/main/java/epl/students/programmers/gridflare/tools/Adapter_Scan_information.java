package epl.students.programmers.gridflare.tools;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import epl.students.programmers.gridflare.R;

public class Adapter_Scan_information extends RecyclerView.Adapter<Adapter_Scan_information.MyViewHolder>{

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView lv_strength;
        TextView lv_room;
        ProgressBar progressBar;

        MyViewHolder(View itemView) {
            super(itemView);
            lv_strength  = itemView.findViewById(R.id.lv_strength);
            lv_room = itemView.findViewById(R.id.lv_room);
            progressBar = itemView.findViewById(R.id.progress_historic);

        }

        void display(Scan_information current_scan){
            assert current_scan != null;
            lv_strength.setText(String.format("%s : %s", String.valueOf(lv_strength.getText()), String.valueOf(current_scan.getStrength() + "%")));
            int prog = current_scan.getStrength();
            lv_room.setText(current_scan.getRoom().toString());
            progressBar.setProgress(prog);
            if(prog > 70)
                progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
            else if(prog > 40)
                progressBar.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
            else
                progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));

        }
    }

    private ArrayList<Scan_information> historic;

    public Adapter_Scan_information(ArrayList<Scan_information> historic){
        this.historic = historic;
    }


    @NonNull
    @Override
    public Adapter_Scan_information.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.listview_items,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Scan_information.MyViewHolder holder, int position) {
        holder.display(historic.get(position));
    }

    @Override
    public int getItemCount() {
        return historic.size();
    }
}
