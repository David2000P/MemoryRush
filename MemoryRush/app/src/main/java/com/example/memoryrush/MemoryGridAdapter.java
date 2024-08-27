package com.example.memoryrush;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MemoryGridAdapter extends BaseAdapter {

    private final int itemCount;
    private boolean isComparing = false;
    private final LayoutInflater inflater;
    private final ArrayList<Integer> cardValues;
    private final boolean[] cardFlipped;
    private final boolean isGameActive;
    private int lastFlippedPosition = -1;
    private int pairsFound = 0;
    private int streak = 1;
    private OnPairsMatchListener listener;
    private final int level;
    private Map<Integer, String> valueToSymbolMap;
    private static final int HEART_PAIR_VALUE = -1;
    private List<Integer> availableSymbols;

    public MemoryGridAdapter(Context context, int pairCount, boolean isGameActive, int level) {
        this.itemCount = pairCount * 2;
        this.inflater = LayoutInflater.from(context);
        this.cardValues = new ArrayList<>();
        this.cardFlipped = new boolean[itemCount];
        this.isGameActive = isGameActive;
        this.level = level;
        initializeValueToSymbolMap();
        initializeCards();
    }

    private void initializeValueToSymbolMap() {
        valueToSymbolMap = new HashMap<>();

        valueToSymbolMap.put(0, "🐶");
        valueToSymbolMap.put(1, "🐱");
        valueToSymbolMap.put(2, "🦁");
        valueToSymbolMap.put(3, "🐯");
        valueToSymbolMap.put(4, "🦊");
        valueToSymbolMap.put(5, "🐻");
        valueToSymbolMap.put(6, "🐼");
        valueToSymbolMap.put(7, "🐨");
        valueToSymbolMap.put(8, "🐸");
        valueToSymbolMap.put(9, "🐵");
        valueToSymbolMap.put(10, "🦄");
        valueToSymbolMap.put(11, "🐢");

        valueToSymbolMap.put(12, "🍎");
        valueToSymbolMap.put(13, "🍌");
        valueToSymbolMap.put(14, "🍒");
        valueToSymbolMap.put(15, "🍇");
        valueToSymbolMap.put(16, "🍉");
        valueToSymbolMap.put(17, "🍓");
        valueToSymbolMap.put(18, "🍍");
        valueToSymbolMap.put(19, "🥥");

        valueToSymbolMap.put(20, "🌟");
        valueToSymbolMap.put(21, "🌞");
        valueToSymbolMap.put(22, "🌜");
        valueToSymbolMap.put(23, "🌍");

        valueToSymbolMap.put(24, "🌷");
        valueToSymbolMap.put(25, "🌻");
        valueToSymbolMap.put(26, "🌼");
        valueToSymbolMap.put(27, "🌵");

        valueToSymbolMap.put(28, "⚽");
        valueToSymbolMap.put(29, "🏀");

        valueToSymbolMap.put(HEART_PAIR_VALUE, "❤️");

        availableSymbols = new ArrayList<>(valueToSymbolMap.keySet());
    }

    private void initializeCards() {
        Random random = new Random();
        boolean addHeartPair = level >= 10 || random.nextInt(100) < 30;

        Collections.shuffle(availableSymbols);

        if (addHeartPair && availableSymbols.contains(HEART_PAIR_VALUE)) {
            cardValues.add(HEART_PAIR_VALUE);
            cardValues.add(HEART_PAIR_VALUE);
            availableSymbols.remove(Integer.valueOf(HEART_PAIR_VALUE));
        }

        for (int i = 0; i < itemCount / 2 - (addHeartPair ? 1 : 0); i++) {
            int symbol = availableSymbols.get(i);
            cardValues.add(symbol);
            cardValues.add(symbol);
        }

        Collections.shuffle(cardValues);
    }

    @Override
    public int getCount() {
        return itemCount;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.card_layout, parent, false);
        }

        TextView cardBack = view.findViewById(R.id.card_back);
        TextView cardFront = view.findViewById(R.id.card_front);

        int cardValue = cardValues.get(position);
        if (valueToSymbolMap.containsKey(cardValue)) {
            cardBack.setText(valueToSymbolMap.get(cardValue));
        }

        if (cardFlipped[position]) {
            cardBack.setVisibility(View.VISIBLE);
            cardFront.setVisibility(View.INVISIBLE);
        } else {
            cardBack.setVisibility(View.INVISIBLE);
            cardFront.setVisibility(View.VISIBLE);
        }

        view.setOnClickListener(v -> {
            if (!isGameActive || isComparing || cardFlipped[position]) return;

            cardFlipped[position] = true;
            notifyDataSetChanged();

            if (lastFlippedPosition == -1) {
                lastFlippedPosition = position;
            } else {
                final int currentPos = position;
                if (cardValues.get(lastFlippedPosition).equals(cardValues.get(currentPos))) {
                    pairsFound++;
                    streak++;
                    if (cardValue == HEART_PAIR_VALUE) {
                        if (listener != null) listener.onHeartPairMatched();
                    } else {
                        if (listener != null) listener.onPairMatched(streak);
                    }
                    lastFlippedPosition = -1;
                    if (pairsFound == itemCount / 2) {
                        if (listener != null) listener.onAllPairsMatched();
                    }
                } else {
                    isComparing = true;
                    new Handler().postDelayed(() -> {
                        cardFlipped[lastFlippedPosition] = false;
                        cardFlipped[currentPos] = false;
                        lastFlippedPosition = -1;
                        streak = 1;
                        isComparing = false;
                        if (listener != null) listener.onPairMismatched();
                        notifyDataSetChanged();
                    }, 1000);
                }
            }
        });

        return view;
    }

    public interface OnPairsMatchListener {
        void onAllPairsMatched();
        void onPairMatched(int streak);
        void onPairMismatched();
        void onHeartPairMatched();
    }

    public void setOnPairsMatchListener(OnPairsMatchListener listener) {
        this.listener = listener;
    }
}
