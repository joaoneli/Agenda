package newdev.agenda.WebServices;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import newdev.agenda.WebServices.content.Data;
import newdev.agenda.WebServices.content.TesteSquidexInfo;
import newdev.agenda.WebServices.content.Token;

public class WebServiceControle {

    /**
     * Responsável por gerenciar as threads que realizam as chamadas web
     */
    private static RequestQueue requestQueue;
    private static Token token;

    public RequestQueue getRequestQueueInstance(Context context)
    {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(context);
        return requestQueue;
    }

    public void carregaListaContatos(final Context context
            , final carregaListaContatosListener carregalistaContatosListener)
    {
        if (token == null)
        {
            geraToken(context, new GeraTokenListener()
            {
                @Override
                public void onTokenOk()
                {
                    carregaListaContatos(context, carregalistaContatosListener);
                }

                @Override
                public void onErro()
                {
                    if (carregalistaContatosListener != null)
                        carregalistaContatosListener.onErro();
                }
            });
        }
        else
        {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    "https://cloud.squidex.io/api/content/pi/pi",
                    null,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response)
                        {
                            if (carregalistaContatosListener != null)
                                carregalistaContatosListener.onResultOk(new Gson().fromJson(response.toString(), TesteSquidexInfo.class));
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            if (carregalistaContatosListener != null)
                                carregalistaContatosListener.onErro();
                        }
                    })
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError
                {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", token.getToken_type() + " " + token.getAccess_token());
                    return headers;
                }
            };
            getRequestQueueInstance(context).add(jsonObjectRequest);
        }
    }

    public void criaLista(final Context context, final Data data, final UpdateListener criaListaListener) throws JSONException
    {
        if (token == null)
        {
            geraToken(context, new GeraTokenListener()
            {
                @Override
                public void onTokenOk()
                {
                    try
                    {
                        criaLista(context, data, criaListaListener);
                    }
                    catch (JSONException e)
                    {
                        if (criaListaListener != null)
                            criaListaListener.onErro();
                    }
                }

                @Override
                public void onErro()
                {
                    if (criaListaListener != null)
                        criaListaListener.onErro();
                }
            });
        }
        else
        {
            JsonObjectRequest jsonObjectRequest
                    = new JsonObjectRequest(Request.Method.POST,
                    "https://cloud.squidex.io/api/content/pi/pi?publish=true",
                    new JSONObject(new Gson().toJson(data)),
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response)
                        {
                            if (criaListaListener != null)
                                criaListaListener.onResultOk();
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            if (criaListaListener != null)
                                criaListaListener.onErro();
                        }
                    })
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError
                {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", token.getToken_type() + " " + token.getAccess_token());
                    return headers;
                }
            };
            getRequestQueueInstance(context).add(jsonObjectRequest);
        }
    }

    public void atualizaLista(final Context context, final Data data, final String id, final UpdateListener atualizaListaListener) throws JSONException
    {
        if (token == null)
        {
            geraToken(context, new GeraTokenListener()
            {
                @Override
                public void onTokenOk()
                {
                    try
                    {
                        atualizaLista(context, data, id, atualizaListaListener);
                    }
                    catch (JSONException e)
                    {
                        if (atualizaListaListener != null)
                            atualizaListaListener.onErro();
                    }
                }

                @Override
                public void onErro()
                {
                    if (atualizaListaListener != null)
                        atualizaListaListener.onErro();
                }
            });
        }
        else
        {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT,
                    "https://cloud.squidex.io/api/content/pi/pi/" + id,
                    new JSONObject(new Gson().toJson(data)),
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response)
                        {
                            if (atualizaListaListener != null)
                                atualizaListaListener.onResultOk();
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            if (atualizaListaListener != null)
                                atualizaListaListener.onErro();
                        }
                    })
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError
                {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", token.getToken_type() + " " + token.getAccess_token());
                    return headers;
                }
            };
            getRequestQueueInstance(context).add(jsonObjectRequest);
        }
    }

    public void deletaLista(final Context context, final String id, final UpdateListener deleteListaListener)
    {
        if (token == null)
        {
            geraToken(context, new GeraTokenListener()
            {
                @Override
                public void onTokenOk()
                {

                    deletaLista(context, id, deleteListaListener);

                }

                @Override
                public void onErro()
                {
                    if (deleteListaListener != null)
                        deleteListaListener.onErro();
                }
            });
        }
        else
        {
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE,
                    "https://cloud.squidex.io/api/content/pi/pi/" + id,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {
                            if (deleteListaListener != null)
                                deleteListaListener.onResultOk();
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            if (deleteListaListener != null)
                                deleteListaListener.onErro();
                        }
                    })
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError
                {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", token.getToken_type() + " " + token.getAccess_token());
                    return headers;
                }
            };
            getRequestQueueInstance(context).add(stringRequest);
        }
    }

    private void geraToken(Context context, final GeraTokenListener geraTokenListener)
    {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                "https://cloud.squidex.io/identity-server/connect/token",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        token = new Gson().fromJson(response, Token.class);
                        if (geraTokenListener != null)
                            geraTokenListener.onTokenOk();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        if (geraTokenListener != null)
                            geraTokenListener.onErro();
                    }
                }
        )
        {
            @Override
            public String getBodyContentType()
            {
                return "application/x-www-form-urlencoded";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("grant_type", "client_credentials");
                params.put("client_id", "pi:newdev");
                params.put("client_secret", "xkCq3nfd/NSIhuKxNdYXbAoDDZrYKH8VJCs2kXceTcg=");
                params.put("scope", "squidex-api");
                return params;
            }
        };
        //
        getRequestQueueInstance(context).add(stringRequest);
    }


    public interface GeraTokenListener
    {
        public abstract void onTokenOk();

        public abstract void onErro();
    }

    public interface carregaListaContatosListener
    {
        public abstract void onResultOk(TesteSquidexInfo teste);

        public abstract void onErro();
    }

    public interface UpdateListener
    {
        public abstract void onResultOk();

        public abstract void onErro();
    }

}
