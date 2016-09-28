package br.com.tads.estarcliente.alarm;

/**
 * Created by Dev_Maker on 21/09/2016.
 */
public abstract class CountDownBehavior implements CountDown.CountDownListener {

    private final long alarmTime;
    private final String displayFormat;

    public CountDownBehavior(long alarmTime, String displayFormat){

        //Valor em segundos no qual deve ser chamado onAlarm().
        this.alarmTime = alarmTime;
        //Formato da string passada ao displayTimeLeft().
        this.displayFormat = displayFormat;
    }

    @Override
    public void onChange(long timeLeft) {
        //Aqui é implementado o comportamento que queremos ter enquanto
        //o CountDown "conta".

        //Deve informar quando chegar a altura de accionar o alarma.
        if(timeLeft == alarmTime)
        {
            onAlarm();
        }
        //Informa o valor actual do contador, com o formato indicado por displayFormat.
        displayTimeLeft(CountDown.secondsToString(timeLeft, displayFormat));

    }

    //Metodos a implementar em resposta ao comportamento.
    protected abstract void onAlarm();
    protected abstract void displayTimeLeft(String timeLeft);
}
