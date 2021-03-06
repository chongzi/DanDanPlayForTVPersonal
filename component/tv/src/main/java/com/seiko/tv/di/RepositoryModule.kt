package com.seiko.tv.di

import com.seiko.tv.data.api.DanDanApiService
import com.seiko.tv.data.comments.*
import com.seiko.tv.data.db.AppDatabase
import com.seiko.tv.data.db.dao.*
import com.squareup.moshi.Moshi
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Provides
    @ActivityRetainedScoped
    fun provideDanDanApiRepository(
        api: Lazy<DanDanApiService>,
        httpDbCache: HttpDbCacheRepository
    ): DanDanApiRepository {
        return DanDanApiRepository(api, httpDbCache)
    }

    @Provides
    @ActivityRetainedScoped
    fun provideHttpDbCacheRepository(
        httpDbCachedDao: HttpDbCacheDao,
        moshi: Moshi
    ): HttpDbCacheRepository {
        return HttpDbCacheRepository(
            httpDbCachedDao,
            moshi
        )
    }

    @Provides
    @ActivityRetainedScoped
    fun provideBangumiDetailsRepository(
        bangumiDetailsDao: BangumiDetailsDao,
        bangumiEpisodeDao: BangumiEpisodeDao,
        bangumiIntroDao: BangumiIntroDao,
        bangumiTagDao: BangumiTagDao
    ): BangumiDetailsRepository {
        return BangumiDetailsRepository(
            bangumiDetailsDao,
            bangumiEpisodeDao,
            bangumiIntroDao,
            bangumiTagDao
        )
    }

    @Provides
    @ActivityRetainedScoped
    fun provideBangumiHistoryRepository(
        database: AppDatabase
    ): BangumiHistoryRepository {
        return BangumiHistoryRepository(
            database.bangumiHistoryDao()
        )
    }

    @Provides
    @ActivityRetainedScoped
    fun provideBangumiKeyboardRepository(
        bangumiKeyBoardDao: BangumiKeyBoardDao
    ): BangumiKeyboardRepository {
        return BangumiKeyboardRepository(
            bangumiKeyBoardDao
        )
    }

}