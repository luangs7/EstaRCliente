package br.com.tads.estarcliente.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.support.v7.widget.CardView;
import android.widget.TextView;

import br.com.tads.estarcliente.R;
import br.com.tads.estarcliente.model.Estar;

public class HistoricoAdapter extends BaseAdapter {

    private List<Estar> objects = new ArrayList<Estar>();

    private Context context;
    private LayoutInflater layoutInflater;

    public HistoricoAdapter(Context context, List<Estar> objects) {
        this.context = context;
        this.objects = objects;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Estar getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.adapter_historico, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((Estar)getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(Estar object, ViewHolder holder) {

    holder.txtEndereco.setText(object.getEndereco());
    holder.txtPlaca.setText(object.getPlaca());
        if(Integer.parseInt(object.getDiff()) > 24){
            holder.txtTempo.setText( object.getDays() + " dias atras");
        }if(Integer.parseInt(object.getDiff()) == 0 ){
            holder.txtTempo.setText( " Alguns minutos atras");
        }
        else{
            holder.txtTempo.setText( object.getHours() + " horas atras");
        }




    }

    public class ViewHolder {
    private CardView CardView;
    private TextView txtEndereco;
    private TextView txtPlaca;
    private TextView txtTempo;
    private TextView textView2;
    private TextView textView3;

         ViewHolder(View view) {
             CardView = (CardView) view.findViewById(R.id.view);
            txtEndereco = (TextView) view.findViewById(R.id.txtEndereco);
            txtPlaca = (TextView) view.findViewById(R.id.txtPlaca);
            txtTempo = (TextView) view.findViewById(R.id.txtTempo);
            textView2 = (TextView) view.findViewById(R.id.textView2);
            textView3 = (TextView) view.findViewById(R.id.textView3);
        }
    }
}
