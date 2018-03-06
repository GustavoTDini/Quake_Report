/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.quakereport.QueryUtils.fetchEarthquakeData;

public class EarthquakeActivity extends AppCompatActivity {

    /** URL to query the USGS dataset for earthquakes information */
    private static final String USGS_REQUEST_URL =
            "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    /** Adapter da lista de earthquakes */
    private EarthquakeArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        mAdapter = new EarthquakeArrayAdapter(this, new ArrayList <Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        EarthquakeASyncTask task = new EarthquakeASyncTask();

        task.execute( USGS_REQUEST_URL );

        earthquakeListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
                Earthquake thisQuake = mAdapter.getItem( position );
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(thisQuake.getURL()));
                startActivity(browserIntent);
            }
        } );
    }

    @SuppressLint("StaticFieldLeak")
    protected class EarthquakeASyncTask extends AsyncTask<String, Void, List<Earthquake>>{

        @Override
        protected List<Earthquake> doInBackground(String... urls) {
            // Não realiza a requisição se não há URLs, ou a primeira URL é nula.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            return fetchEarthquakeData(urls[0]);
        }
        /**
         * Este método roda na thread main UI após o trabalho em segundo plano estiver
         * completo. Este método recebe como entrada, o valor de retorno do método doInBackground().
         * Primeiro limpamos o adapter, para se livrar dos dados do earthquake de uma anterior
         * busca ao USGS. Então atualizamos o adapter com a nova lista de earthquakes,
         * que irá chamar a ListView para re-popular seus itens de lista.
         */
        @Override
        protected void onPostExecute(List<Earthquake> data) {
            // Limpa o adapter de dados anteriores de earthquake
            mAdapter.clear();

            // Se há uma lista válida de {@link Earthquake}s, adiciona-os ao data set do adapter.
            // Isto irá chamar a ListView para ser atualizada.
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }
        }
    }
}
