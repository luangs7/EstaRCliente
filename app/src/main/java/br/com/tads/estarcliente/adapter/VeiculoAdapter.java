package br.com.tads.estarcliente.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.tads.estarcliente.R;
import br.com.tads.estarcliente.model.Veiculo;


/**
 * Created by DevMaker on 3/16/16.
 */
public class VeiculoAdapter extends BaseAdapter {

    private final Context mContext;
    private List<Veiculo> veiculos;


    public VeiculoAdapter(Context context, List<Veiculo> veiculos) {
        this.mContext = context;
        this.veiculos = veiculos;

    }

    @Override
    public int getCount() {
        return veiculos.size();
    }

    @Override
    public Veiculo getItem(int position) {
        return veiculos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        final Veiculo veiculo = veiculos.get(position);


        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_veiculos, parent, false);

            holder = new ViewHolder();
            holder.placa = (TextView) convertView.findViewById(R.id.txtPlaca);
            holder.modelo = (TextView) convertView.findViewById(R.id.txtModelo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.placa.setText(veiculo.getPlaca());
        holder.modelo.setText(veiculo.getModelo());

        return convertView;
    }

    class ViewHolder {
        public TextView placa;
        public TextView modelo;
    }

}
