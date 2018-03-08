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

import android.content.Intent;
import android.net.Uri;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.quakereport.QueryUtils.fetchEarthquakeData;

public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<List<Earthquake>>{

    /**
     * Valor constante para o ID do loader de earthquake. Podemos escolher qualquer inteiro.
     * Isto só importa realmente se você estiver usando múltiplos loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;

    /** URL to query the USGS dataset for earthquakes information */
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";

    /** Adapter da lista de earthquakes */
    private EarthquakeArrayAdapter mAdapter;

    /** TextView que é mostrada quando a lista é vazia */
    private TextView mEmptyStateTextView;

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

        mEmptyStateTextView = (TextView) findViewById(R.id.no_quake_text);
        earthquakeListView.setEmptyView(mEmptyStateTextView);

        // Obtém uma referência ao LoaderManager, a fim de interagir com loaders.
        LoaderManager loaderManager = getLoaderManager();

        // Inicializa o loader. Passa um ID constante int definido acima e passa nulo para
        // o bundle. Passa esta activity para o parâmetro LoaderCallbacks (que é válido
        // porque esta activity implementa a interface LoaderCallbacks).
        loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);

        earthquakeListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
                Earthquake thisQuake = mAdapter.getItem( position );
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(thisQuake.getURL()));
                startActivity(browserIntent);
            }
        } );
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        // Seta o texto de estado vazio para mostrar "Nenhum terremoto encontrado."
        mEmptyStateTextView.setText(R.string.no_earthquakes);

        // Limpa o adapter de dados de earthquake anteriores
        mAdapter.clear();

        // Se há uma lista válida de {@link Earthquake}s, então os adiciona ao data set do adapter.
        // Isto ativará a atualização da ListView.
        if (earthquakes != null && !earthquakes.isEmpty()) {
            mAdapter.addAll(earthquakes);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        // Reseta o Loader, então podemos limpar nossos dados existentes.
        mAdapter.clear();
    }

}
