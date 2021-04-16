package com.tutorial.foody.data

import com.tutorial.foody.data.network.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class FoodyRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {
}