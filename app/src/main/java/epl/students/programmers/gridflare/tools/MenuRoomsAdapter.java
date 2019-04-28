package epl.students.programmers.gridflare.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import epl.students.programmers.gridflare.ORM.DatabaseManager;
import epl.students.programmers.gridflare.R;

/*
Le plus simple ca va etre de faire deux adapter et de mettre dans le template de place un recycler view pour que de lui meme il aille cherhcer les pieces
Ca va pas etre simple mais ca sera le mieux je pense
 */

public class MenuRoomsAdapter extends RecyclerView.Adapter<MenuRoomsAdapter.LocalViewHolder> {

    class LocalViewHolder extends RecyclerView.ViewHolder{

        View room_template;
        TextView room_name;
        TextView room_floor;

        LocalViewHolder(View itemView) {
            super(itemView);
            room_template = itemView;
            room_floor = room_template.findViewById(R.id.d_room_floor);
            room_name = room_template.findViewById(R.id.d_room_name);
        }

        @SuppressLint("SetTextI18n")
        void display(Room room){
            assert room != null;
            room_name.setText(room.getRoom_name());
            room_floor.setText(room.getFloor() + "");
            room_template.setTag(room.getRoomID());//A voir si le tag va bien s'appliquer
        }
    }

    private ArrayList<Room> rooms;

    MenuRoomsAdapter(ArrayList<Room> rooms){
        this.rooms = rooms;
    }

    @NonNull
    @Override
    public MenuRoomsAdapter.LocalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View templates = layoutInflater.inflate(R.layout.template_rooms,parent,false);
        return new MenuRoomsAdapter.LocalViewHolder(templates);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuRoomsAdapter.LocalViewHolder holder, int position) {
        holder.display(rooms.get(position));
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }
}
