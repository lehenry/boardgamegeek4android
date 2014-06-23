package com.boardgamegeek.io;

import java.io.IOException;

import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RestAdapter.Builder;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;

import com.boardgamegeek.auth.Authenticator;
import com.boardgamegeek.io.xml.SimpleXMLConverter;

public class Adapter {
	public static BggService create() {
		return new RestAdapter.Builder().setEndpoint("http://www.boardgamegeek.com/")
			.setConverter(new SimpleXMLConverter()).build().create(BggService.class);
	}

	public static BggService createWithAuth(Context context) {
		return createBuilderWithAuth(context).build().create(BggService.class);
	}

	public static BggService createWithAuthRetry(Context context) {
		ErrorHandler errorHandler = new ErrorHandler() {
			@Override
			public Throwable handleError(RetrofitError cause) {
				Response r = cause.getResponse();
				if (r != null && r.getStatus() == 202) {
					return new RetryableException(cause);
				}
				return cause;
			}
		};

		return createBuilderWithAuth(context).setErrorHandler(errorHandler).build().create(BggService.class);
	}

	private static Builder createBuilderWithAuth(Context context) {
		RequestInterceptor requestInterceptor = null;

		AccountManager accountManager = AccountManager.get(context);
		final Account account = Authenticator.getAccount(accountManager);
		try {
			final String authToken = accountManager.blockingGetAuthToken(account, Authenticator.AUTHTOKEN_TYPE, true);
			requestInterceptor = new RequestInterceptor() {
				@Override
				public void intercept(RequestFacade request) {
					request.addHeader("Cookie", "bggusername=" + account.name + "; bggpassword=" + authToken);
				}
			};
		} catch (OperationCanceledException | AuthenticatorException | IOException e) {
			// TODO handle this somehow; maybe just return create()
		}

		return new RestAdapter.Builder().setEndpoint("http://www.boardgamegeek.com/")
			.setRequestInterceptor(requestInterceptor).setConverter(new SimpleXMLConverter());
	}
}
