package biba.bicicleta.publica.badajoz;

import java.util.Vector;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MiAdaptador extends BaseAdapter {
	private final Activity actividad;

	private final Vector<Estacion> lista;

	public MiAdaptador(Activity actividad, Vector<Estacion> lista) {
		super();
		this.actividad = actividad;
		this.lista = lista;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		// notifyDataSetChanged();
		ViewHolder holder;
		LayoutInflater inflater = actividad.getLayoutInflater();
		View vi = convertView;

		// View Recycling
		if (vi == null || vi.getTag() == null) {
			vi = inflater.inflate(R.layout.elemento_lista, null);
			holder = getHolder(vi);
			vi.setTag(holder);
		} else {
			// View Holder pattern
			holder = (ViewHolder) vi.getTag();
		}

		int rojo = Color.rgb(245, 110, 119);
		int verde = Color.rgb(182, 254, 185);
		// if (position % 2 == 0) {
		// vi.setBackgroundColor(Color.rgb(grisoscuro, grisoscuro, grisoscuro));
		// } else {
		// vi.setBackgroundColor(Color.rgb(grisclaro, grisclaro, grisclaro));
		// }

		if (position % 2 == 0) {
			vi.setBackgroundResource(R.drawable.selector1);
		} else {
			vi.setBackgroundResource(R.drawable.selector2);
		}

		String estado = lista.elementAt(position).getEstado();
		holder.nombre.setText(lista.elementAt(position).getNombre());
		if (estado.indexOf("FUERA") != -1) {
			holder.nombre.setTextColor(rojo);
		} else {
			holder.nombre.setTextColor(verde);

		}

		holder.numero.setText(Integer.toString(lista.elementAt(position)
				.getNumero()));

		holder.numero.setTextColor(Color.WHITE);

		int bicis = lista.elementAt(position).getDisponibles();
		holder.bicis.setText(Integer.toString(bicis));
		if (bicis == 0) {
			holder.bicis.setTextColor(rojo);
		} else {
			holder.bicis.setTextColor(Color.WHITE);
		}

		int parking = lista.elementAt(position).getEspacio();
		holder.parking.setText(Integer.toString(parking));
		if (parking == 0) {
			holder.parking.setTextColor(rojo);
		} else {
			holder.parking.setTextColor(Color.WHITE);
		}

		// vi.set
		// vi.setLabelFor(lista.elementAt(position).getNumero() - 1);
		return vi;

	}

	public int getNumber() {
		return 5;
	}

	public int getCount() {

		return lista.size();

	}

	public Object getItem(int arg0) {

		return lista.elementAt(arg0);

	}

	public long getItemId(int position) {

		return position;

	}

	private ViewHolder getHolder(View vi) {
		ViewHolder holder = new ViewHolder();
		holder.numero = (TextView) vi.findViewById(R.id.numero);
		holder.nombre = (TextView) vi.findViewById(R.id.titulo);
		holder.bicis = (TextView) vi.findViewById(R.id.bicis);
		holder.parking = (TextView) vi.findViewById(R.id.parking);

		return holder;
	}

	public static class ViewHolder {
		TextView numero;
		TextView nombre;
		TextView bicis;
		TextView parking;
	}

}