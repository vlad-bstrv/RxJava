package com.vladbstrv.rxjava

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnClick = findViewById<Button>(R.id.btnTest)
            btnClick.setOnClickListener {
            Log.e(TAG, "click click")
        }

        //источник данных
        //базовый класс
        val observable = Observable.just(1, 2, 3)
        //подписка на источник данных
//        val dispose = observable.subscribe {
//            Log.e(TAG, "new data $it")
//        }

        val single = Single.just(1)

        val flowable = Flowable.just(1, 2, 3)

        val disposeFlowable = flowable.subscribe({
            Log.e(TAG, "new data $it")
        }, {

        })

        val dispose = dataSource()
                //subscribeOn - что бы источник данных(dataSource) работал в фоновом потоке
            .subscribeOn(Schedulers.newThread())
                //observeOn - указывает все что просходит в ветке subscribe
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                btnClick.text = "Next int $it"
                Log.e(TAG, "next int $it")
            }, {
                Log.e(TAG, "it $it")
            }, {

            })
    }

    fun dataSource(): Flowable<Int> {
        return Flowable.create ({ subscriber ->
            for(i in 0..100000) {
                subscriber.onNext(i)
            }
            subscriber.onComplete()
        }, BackpressureStrategy.BUFFER)
    }
}