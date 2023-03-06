package com.discordgroupbanknotifications;

import net.runelite.client.RuneLite;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class ApiTool
{
    private static ApiTool _instance;

    private OkHttpClient httpClient = null;

    public static ApiTool getInstance()
    {
        if (_instance == null)
        {
            _instance = new ApiTool();
            _instance.httpClient = new OkHttpClient.Builder()
                    .cache(new Cache(new File(RuneLite.CACHE_DIR, "okhttp_drdn"), 20 * 1024 * 1024)) // 20mb cache
                    .build();
        }
        return _instance;
    }

    public CompletableFuture<ResponseBody> postRaw(String url, String data, String type)
    {
        Request request = new Request.Builder().url(url).post(RequestBody.create(MediaType.parse(type), data)).build();

        return callRequest(request);
    }

    private CompletableFuture<ResponseBody> callRequest(Request request)
    {
        CompletableFuture<ResponseBody> future = new CompletableFuture<>();

        httpClient.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                try (ResponseBody responseBody = response.body())
                {
                    if (!response.isSuccessful())
                    {
                        future.completeExceptionally(new IOException("Unexpected code " + response));
                    }
                    else
                    {
                        future.complete(responseBody);
                    }
                }
                response.close();
            }
        });

        return future;
    }
}
