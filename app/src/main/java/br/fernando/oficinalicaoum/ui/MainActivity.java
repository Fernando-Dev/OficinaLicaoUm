package br.fernando.oficinalicaoum.ui;


import android.annotation.SuppressLint;

import android.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;


import android.widget.ImageButton;
import android.widget.ListView;

import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.Random;


import androidx.appcompat.app.AppCompatActivity;

import br.fernando.oficinalicaoum.DAO.DAO;
import br.fernando.oficinalicaoum.R;

import br.fernando.oficinalicaoum.modelo.Pedido;


public class MainActivity extends AppCompatActivity {

    private ImageButton sincronizaPedido, limpaPedido;
    private ListView listaPedido;
    private List<Map<String, Object>> pedidos;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sincronizaPedido = findViewById(R.id.btn_sincronizar);
        limpaPedido = findViewById(R.id.btn_limpar);

        listaPedido = findViewById(R.id.lv_lista_pedidos);

        sincronizaPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new sincronizaPedidosThread().execute((Void[]) null);
            }
        });
        limpaPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Limpar Pedidos")
                        .setMessage("Você tem certeza em limpar todos os pedidos?")
                        .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (pedidos == null) {
                                    Toast.makeText(MainActivity.this, "Lista está vazia!", Toast.LENGTH_SHORT).show();
                                } else if (pedidos != null && pedidos.size() == 0) {
                                    Toast.makeText(MainActivity.this, "Lista está vazia!", Toast.LENGTH_SHORT).show();
                                } else if (pedidos != null) {
                                    new limpaPedidosThread().execute((Void[]) null);
                                }
                            }
                        })
                        .setNegativeButton("NÃO", null)
                        .show();
            }
        });
    }

    private class sincronizaPedidosThread extends AsyncTask<Void, Void, List<Map<String, Object>>> {
        private ProgressDialog bar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bar = new ProgressDialog(MainActivity.this);
            bar.setIndeterminate(false);
            bar.setMessage("Carregando... Por favor aguarde");
            bar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            bar.setCancelable(false);
            bar.show();
        }

        @Override
        protected List<Map<String, Object>> doInBackground(Void... voids) {
            pedidos = new ArrayList<Map<String, Object>>();
            Random random = new Random();
            DAO dao = new DAO();
            try {
                List<Pedido> listaPedido = dao.adicionaPedido();
                for (Pedido pedido : listaPedido) {
                    String mili = String.valueOf(random.nextInt(2000));
                    Long miliSegundos = Long.valueOf(mili);
                    Thread.sleep(miliSegundos);
                    Map<String, Object> item = new HashMap<String, Object>();
                    item.put("id", "000" + pedido.getId());
                    DecimalFormat formatador = new DecimalFormat("0.##");
                    item.put("valor", formatador.format(pedido.getValor()));
                    pedidos.add(item);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return pedidos;
        }

        @Override
        protected void onPostExecute(List<Map<String, Object>> maps) {
            super.onPostExecute(maps);
            bar.dismiss();
            String[] de = {"id", "valor"};
            int[] para = {R.id.txt_id_pedido, R.id.txt_valor_pedido};
            SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, maps, R.layout.item_lista_pedido, de, para);
            listaPedido.setAdapter(adapter);
        }
    }

    private class limpaPedidosThread extends AsyncTask<Void, Void, List<Map<String, Object>>> {

        @Override
        protected List<Map<String, Object>> doInBackground(Void... voids) {
            return limpaPedidos();
        }

        @Override
        protected void onPostExecute(List<Map<String, Object>> maps) {
            super.onPostExecute(maps);
            String[] de = {"id", "valor"};
            int[] para = {R.id.txt_id_pedido, R.id.txt_valor_pedido};
            SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, maps, R.layout.item_lista_pedido, de, para);
            listaPedido.setAdapter(adapter);
        }
    }


    public List<Map<String, Object>> geraPedidos() {
        pedidos = new ArrayList<Map<String, Object>>();
        DAO dao = new DAO();
        List<Pedido> listaPedido = dao.adicionaPedido();
        for (Pedido pedido : listaPedido) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("id", "000" + pedido.getId());
            DecimalFormat formatador = new DecimalFormat("###.##");
            item.put("valor", formatador.format(pedido.getValor()));
            pedidos.add(item);
        }
        return pedidos;
    }


    public List<Map<String, Object>> limpaPedidos() {
        pedidos.clear();
        return pedidos;
    }


}
