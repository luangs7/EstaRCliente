package br.com.tads.estarcliente.alarm;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.format.DateFormat;

import java.lang.ref.WeakReference;

/**
 * Created by Dev_Maker on 21/09/2016.
 */
public class CountDown {

    //Interface a ser implementada por um listener
    public interface CountDownListener {
        //Chamado quando o valor de secondsLeft é alterado,
        //quando for decrementado ou incrementado.
        void onChange(long timeLeft);
        //Chamado quando o contador chegar ao fim.
        void onEnd();
    }

    private long fromSeconds;
    private long secondsLeft;
    private CountDownListener listener;
    private boolean isCounting = false;

    //Valor em milissegundos de um segundo.
    private static final long ONE_SECOND = 1000;
    private static final int MSG = 1;

    //Constrói o contador com o valor inicial de segundos.
    public CountDown(long fromSeconds){

        this.fromSeconds = fromSeconds;
        handler = new CountDownHandler(this);
    }

    //Inicia a contagem, a partir do valor inícial.
    public synchronized void start(){
        if(isCounting){
            return;//ou talvez lançar uma excepção
        }
        isCounting = true;
        secondsLeft = fromSeconds;
        handler.sendMessage(handler.obtainMessage(MSG));
    }

    public void finish(){
        listener.onEnd();
    }

    //Pára a contagem.
    public synchronized void stop(){
        if(!isCounting){
            return;//ou talvez lançar uma excepção
        }
        isCounting = false;
        handler.removeMessages(MSG);
    }

    //Retoma a contagem.
    public synchronized void resume(){
        if(isCounting || secondsLeft == 0){
            return;//ou talvez lançar uma excepção
        }
        isCounting = true;
        handler.sendMessageDelayed(handler.obtainMessage(MSG), ONE_SECOND);
    }

    //Incrementa o valor do contador.
    public synchronized long increaseBy(long fromSeconds){
        secondsLeft += fromSeconds;
        return secondsLeft;
    }

    //true se o contador estiver contando.
    public boolean isCounting(){
        return isCounting;
    }

    //Guarda um listener.
    public void setCountDownListener(CountDownListener listener){
        this.listener = listener;
    }

    //Método para formatar um valor em segundos em algo tipo "mm:ss" ou "HH:mm:ss".
    public static String secondsToString(long seconds, String format){
        return DateFormat.format(format, seconds * ONE_SECOND).toString();
    }

    private final Handler handler;

    //Handler para controlar o contador
    private static class CountDownHandler extends Handler
    {

        private final WeakReference<CountDown> countDownWeakReference;

        private CountDownHandler(CountDown countDownInstance) {
            countDownWeakReference = new WeakReference<>(countDownInstance);
        }

        @Override
        public void handleMessage(Message msg) {

            CountDown countDown = countDownWeakReference.get();
            if(countDown == null){
                return;
            }

            synchronized (countDown) {

                //Guarda o instante em que inicia o processamento.
                long tickStart = SystemClock.elapsedRealtime();

                //Se tiver sido parado sai.
                if(!countDown.isCounting){
                    return;
                }

                //Notifica o listener com o segundos que faltam para terminar.
                if (countDown.listener != null) {
                    countDown.listener.onChange(countDown.secondsLeft);
                }

                //O contador chegou ao fim, notifica o listener.
                if (countDown.secondsLeft == 0) {
                    countDown.isCounting = false;
                    if (countDown.listener != null) {
                        countDown.listener.onEnd();
                    }
                } else {
                    //decrementa o contador.
                    countDown.secondsLeft--;

                    //Obtém o tempo para o próximo decremento.
                    //Leva em conta o tempo gasto no processamento,
                    //principalmente o eventualmente gasto pela implementação
                    // do método onChange() no listener.
                    long delay = ONE_SECOND - (SystemClock.elapsedRealtime() - tickStart);

                    //Se o tempo gasto for superior a um segundo, ajusta-o para o próximo.
                    //Se o tempo gasto no método onChange() for próximo ou
                    // superior a um segundo ele só será chamado no próximo.
                    while(delay < 0){
                        countDown.secondsLeft--;
                        delay += ONE_SECOND;
                    }
                    //Garante o término se o tempo for excedido
                    if(countDown.secondsLeft < 0){
                        countDown.listener.onEnd();
                    }else {
                        //Agenda o próximo decremento.
                        sendMessageDelayed(obtainMessage(MSG), delay);
                    }
                }
            }
        }
    };
}
