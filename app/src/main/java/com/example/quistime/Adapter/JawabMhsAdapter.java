package com.example.quistime.Adapter;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quistime.Menu.JawabActivity;
import com.example.quistime.Models.Nilai;
import com.example.quistime.Models.SoalDosen;
import com.example.quistime.R;

import java.util.ArrayList;

public class JawabMhsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;

    private ArrayList<SoalDosen> daftarSoal;
    private Context context;
    JawabActivity listener;
    int No =0;
    int nilai;

    public int getNilai() {
        return nilai;
    }

    public void setNilai(int nilai) {
        this.nilai = this.nilai+nilai;
    }

    public JawabMhsAdapter(ArrayList<SoalDosen> daftarSoal, Context context) {
        this.daftarSoal = daftarSoal;
        this.context = context;
        this.listener = (JawabActivity) context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.jawabsoal, parent, false);
            return new ItemViewHolder(itemView);
        } else if (viewType == TYPE_HEADER) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false);
            return new HeaderViewHolder(itemView);
        } else if (viewType == TYPE_FOOTER) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer, parent, false);
            return new FooterViewHolder(itemView);
        } else return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            footerHolder.btnFooter.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View view) {
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                    String txtTanggal = sdf.format(cal.getTime());
                    float hasil = (getNilai()*100)/(daftarSoal.size()-1);
                    String score = Float.toString(hasil);
                    Nilai n = new Nilai(score, txtTanggal);
                    listener.onSaveData(n);
                }
            });
        } else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            SoalDosen soalDosen = daftarSoal.get(position);
            String urutan = Integer.toString(No+1);
            itemViewHolder.txtNosoal.setText(urutan+". ");
            itemViewHolder.txtSoalMhs.setText(soalDosen.getSoal());
            itemViewHolder.rButtonA.setText("A. "+soalDosen.getA());
            itemViewHolder.rButtonB.setText("B. "+soalDosen.getB());
            itemViewHolder.rButtonC.setText("C. "+soalDosen.getC());
            itemViewHolder.rButtonD.setText("D. "+soalDosen.getD());
            itemViewHolder.rButtonE.setText("E. "+soalDosen.getE());

            final SoalDosen mk = daftarSoal.get(position);
            final String kunci = mk.getJawaban();

            itemViewHolder.btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemViewHolder.rButton.getCheckedRadioButtonId()== -1){
                    Toast.makeText(context, "Pilih Jawaban Sebelum Disimpan", Toast.LENGTH_SHORT).show();
                }else {
                    itemViewHolder.btnSimpan.setText("Tersimpan");
                    itemViewHolder.btnSimpan.setEnabled(false);
                    itemViewHolder.rButtonA.setEnabled(false);
                    itemViewHolder.rButtonB.setEnabled(false);
                    itemViewHolder.rButtonC.setEnabled(false);
                    itemViewHolder.rButtonD.setEnabled(false);
                    itemViewHolder.rButtonE.setEnabled(false);
                    String result = (itemViewHolder.rButtonA.isChecked()) ? "A" : (itemViewHolder.rButtonB.isChecked()) ? "B" : (itemViewHolder.rButtonC.isChecked()) ? "C" : (itemViewHolder.rButtonD.isChecked()) ? "D" : (itemViewHolder.rButtonE.isChecked()) ? "E" : "";
                    if (kunci.equals(result)) {
                        setNilai(1);
                        System.out.println("Nilai = " + getNilai());
                    }
                }
            }
        });
        No = No+1;
        }
    }

    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == daftarSoal.size()) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return daftarSoal.size() +1 ;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtNosoal, txtSoalMhs;
        RadioGroup rButton;
        RadioButton rButtonA, rButtonB, rButtonC, rButtonD, rButtonE;
        Button btnSimpan;
        public ItemViewHolder(View itemView) {
            super(itemView);
            txtNosoal = itemView.findViewById(R.id.txtNoSoal);
            txtSoalMhs = itemView.findViewById(R.id.txtSoalMhs);
            rButton = itemView.findViewById(R.id.rButton);
            rButtonA = itemView.findViewById(R.id.rButtonA);
            rButtonB = itemView.findViewById(R.id.rButtonB);
            rButtonC = itemView.findViewById(R.id.rButtonC);
            rButtonD = itemView.findViewById(R.id.rButtonD);
            rButtonE = itemView.findViewById(R.id.rButtonE);
            btnSimpan = itemView.findViewById(R.id.btnSimpanKunci);
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView txtHeader;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            txtHeader = itemView.findViewById(R.id.txtHeader);
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        Button btnFooter;
        public FooterViewHolder(View view) {
            super(view);
            btnFooter = view.findViewById(R.id.btnFooter);
        }
    }
}