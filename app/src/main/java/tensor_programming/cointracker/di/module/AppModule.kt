package tensor_programming.cointracker.di.module

import dagger.Module
import android.content.Context
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Tensor on 2/7/2018.
 */
@Module
class AppModule(private val context: Context) {
    @Provides
    @Singleton
    fun providesContext(): Context {
        return context
    }
}