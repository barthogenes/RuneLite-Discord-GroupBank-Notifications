package com.discordgroupbanknotifications;

import okhttp3.*;

import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class ApiTool
{
    @Inject
    private OkHttpClient httpClient;

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
