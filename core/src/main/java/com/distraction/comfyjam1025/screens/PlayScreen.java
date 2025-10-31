package com.distraction.comfyjam1025.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.distraction.comfyjam1025.Constants;
import com.distraction.comfyjam1025.Context;
import com.distraction.comfyjam1025.Utils;
import com.distraction.comfyjam1025.entity.ImageEntity;
import com.distraction.comfyjam1025.entity.Leaf;
import com.distraction.comfyjam1025.entity.PuzzlePiece;
import com.distraction.comfyjam1025.entity.TextEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlayScreen extends Screen {

    private static final String[] SCRIPTS = {
        "A flower?\n" +
            "People leave those for the\n" +
            "ones they care about.\n" +
            "Maybe she was just passing by...",
        "That photo...\n" +
            "I kept it close near the end.\n" +
            "It was my most cherished possession.\n" +
            "How did she find it?",
        "I don't know what I did\n" +
            "to deserve gratitude...\n" +
            "but for the first time in a long while,\n" +
            "I don't feel forgotten.",
        "I see it now.\n" +
            "All this time, I thought I had faded.\n" +
            "But she remembered year after year.\n" +
            "Quietly, she carried my heart with her.\n" +
            "And that’s enough."
    };

    private static final int GRID_WIDTH = 120;
    private static final float PAN_SPEED = 30;

    private final int numRows;

    private final TextureRegion puzzleBg;
    private final PuzzlePiece[][] puzzle;
    private PuzzlePiece selected;
    private PuzzlePiece render1;
    private PuzzlePiece render2;

    private final ImageEntity lmb;
    private final ImageEntity rmb;
    private final TextEntity swapText;
    private final TextEntity rotateText;

    private final TextEntity text;
    private final TextureRegion next;
    private float nextTime;

    private boolean done;
    private float doneTime = 6f;

    private float flash = 0;

    private final List<ImageEntity> particles;
    private float leafTime;

    public PlayScreen(Context context, int year) {
        super(context);

        numRows = 3;

        float uix = 50;

        lmb = new ImageEntity(context, context.getImage("mouse"));
        lmb.setPosition(uix, 115);
        lmb.setSize(24, 30);
        rmb = new ImageEntity(context, context.getImage("mouse"));
        rmb.setPosition(uix, 55);
        rmb.setSize(24, 30);
        rmb.hflip = true;
        swapText = new TextEntity(context, context.getFont(Context.CON26), "Swap", uix, 140, TextEntity.HAlignment.CENTER);
        swapText.globalScale = textCam.viewportWidth / cam.viewportWidth;
        rotateText = new TextEntity(context, context.getFont(Context.CON26), "Rotate", uix, 80, TextEntity.HAlignment.CENTER);
        rotateText.globalScale = textCam.viewportWidth / cam.viewportWidth;

        puzzleBg = context.getImage("puzzlebg");
        puzzle = new PuzzlePiece[numRows][numRows];
        int tileSize = GRID_WIDTH / numRows;
        float sx = Constants.WIDTH / 2f - GRID_WIDTH / 2f;
        float sy = Constants.HEIGHT / 2f + GRID_WIDTH / 2f;
        TextureRegion puzzleImage = context.getImage("puzzle" + year);
        TextureRegion[][] images = puzzleImage.split(puzzleImage.getRegionWidth() / numRows, puzzleImage.getRegionHeight() / numRows);
        for (int row = 0; row < puzzle.length; row++) {
            for (int col = 0; col < puzzle[row].length; col++) {
                puzzle[row][col] = new PuzzlePiece(context, row * numRows + col, row, col, images[row][col]);
                puzzle[row][col].setSize(tileSize + 0.1f, tileSize + 0.1f);
            }
        }

        List<PuzzlePiece> flat = new ArrayList<>();
        for (PuzzlePiece[] row : puzzle) {
            flat.addAll(Arrays.asList(row));
        }
        Collections.shuffle(flat);
        Collections.shuffle(flat);

        int index = 0;
        for (int row = 0; row < puzzle.length; row++) {
            for (int col = 0; col < puzzle[row].length; col++) {
                puzzle[row][col] = flat.get(index++);
                puzzle[row][col].setCell(row, col);
                puzzle[row][col].setPosition(
                    sx + col * tileSize + tileSize / 2f,
                    sy - row * tileSize - tileSize / 2f
                );
                puzzle[row][col].randomRotate();
            }
        }

        in = new Transition(context, Transition.Type.FLASH_IN, 1f, () -> ignoreInput = false);
        in.setFlashColor(Color.BLACK);
        in.start();

        if (year == 4) {
            out = new Transition(context, Transition.Type.FLASH_OUT, 1f, () -> context.sm.replace(new IntroScene(context)));
            out.setFlashColor(Color.WHITE);
        } else {
            out = new Transition(context, Transition.Type.FLASH_OUT, 1f, () -> context.sm.replace(new YearScreen(context, year + 1)));
            out.setFlashColor(Color.BLACK);
        }

        text = new TextEntity(context, context.getFont(Context.CON26), SCRIPTS[year - 1], Constants.WIDTH / 2f + 80, Constants.HEIGHT / 2f + GRID_WIDTH / 2f - 10);
        text.setColor(1, 1, 1, 0);
        text.a = 0;
        text.ta = 0;
        text.globalScale = textCam.viewportWidth / cam.viewportWidth;

        next = context.getImage("next");

        particles = new ArrayList<>();
    }

    private void updatePositions() {
        int tileSize = GRID_WIDTH / numRows;
        float sx = Constants.WIDTH / 2f - GRID_WIDTH / 2f;
        float sy = Constants.HEIGHT / 2f + GRID_WIDTH / 2f;
        for (int row = 0; row < puzzle.length; row++) {
            for (int col = 0; col < puzzle[row].length; col++) {
                puzzle[row][col].setDest(
                    sx + col * tileSize + tileSize / 2f,
                    sy - row * tileSize - tileSize / 2f
                );
                puzzle[row][col].setCell(row, col);
            }
        }
    }

    private boolean allDest() {
        for (PuzzlePiece[] puzzlePieces : puzzle) {
            for (PuzzlePiece cell : puzzlePieces) {
                if (!cell.atDestination()) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isDone() {
        for (int row = 0; row < puzzle.length; row++) {
            for (int col = 0; col < puzzle[row].length; col++) {
                PuzzlePiece cell = puzzle[row][col];
                if (cell.id != row * numRows + col || cell.rotateIndex != 0 || !cell.atDestination()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void input() {
        if (ignoreInput) return;

        m.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        unproject();

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (!done) {
                for (PuzzlePiece[] row : puzzle) {
                    for (PuzzlePiece cell : row) {
                        if (!cell.atDestination()) continue;
                        if (cell.contains(m.x, m.y)) {
                            if (selected == null) {
                                selected = cell;
                            } else if (selected == cell) {
                                selected = null;
                            } else {
                                int r1 = selected.row;
                                int c1 = selected.col;
                                int r2 = cell.row;
                                int c2 = cell.col;

                                render1 = puzzle[r1][c1];
                                render2 = puzzle[r2][c2];

                                PuzzlePiece temp = puzzle[r1][c1];
                                puzzle[r1][c1] = puzzle[r2][c2];
                                puzzle[r1][c1].setDest(temp.x, temp.y);
                                puzzle[r2][c2] = temp;
                                puzzle[r2][c2].setDest(puzzle[r1][c1].x, puzzle[r1][c1].y);
                                updatePositions();
                                selected = null;
                                context.audio.playSound("swap", 0.2f);
                            }
                            return;
                        }
                    }
                }
            } else if (doneTime < 0) {
                ignoreInput = true;
                out.start();
            }
        } else if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            if (!done) {
                for (PuzzlePiece[] row : puzzle) {
                    for (PuzzlePiece cell : row) {
                        if (cell.contains(m.x, m.y)) {
                            render1 = cell;
                            cell.rotate();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void update(float dt) {
        in.update(dt);
        out.update(dt);

        if (allDest()) render1 = render2 = null;

        for (PuzzlePiece[] row : puzzle) {
            for (PuzzlePiece cell : row) cell.update(dt);
        }
        if (!done && isDone()) {
            done = true;
            selected = null;
            context.audio.playSound("puzzlefinish", 0.5f);
            flash = 1f;
        }
        flash -= dt;
        if (done) {
            doneTime -= dt;
            if (doneTime < 5) {
                cam.position.x = MathUtils.clamp(cam.position.x + PAN_SPEED * dt, Constants.WIDTH / 2f, Constants.WIDTH / 2f + 80);
                cam.update();
            }
            if (doneTime < 2) {
                text.ta = 1;
                text.update(dt);
            }
            if (doneTime < 0) nextTime += dt;
        }
        leafTime -= dt;
        if (leafTime <= 0) {
            leafTime = MathUtils.random(0.3f, 1.4f);
            float x = MathUtils.random(20, Constants.WIDTH + 80);
            float y = Constants.HEIGHT + 10;
            particles.add(new Leaf(context, context.getImage("leaves"), x, y, -1f * MathUtils.random(1, 3), -1f * MathUtils.random(3, 6)));
        }
        for (int i = particles.size() - 1; i >= 0; i--) {
            ImageEntity p = particles.get(i);
            p.update(dt);
            if (p.remove) particles.remove(p);
        }
        textCam.position.x = cam.position.x * text.globalScale;
        textCam.update();
    }

    @Override
    public void render() {
        sb.begin();

        sb.setProjectionMatrix(uiCam.combined);
        sb.setColor(Constants.PUZZLE_BG);
        sb.draw(pixel, 0, 0, Constants.WIDTH, Constants.HEIGHT);

        sb.setProjectionMatrix(cam.combined);
        for (ImageEntity p : particles) p.render(sb);

        sb.setColor(1, 1, 1, 1);
        if (!done) {
            sb.setProjectionMatrix(textCam.combined);
            swapText.render(sb);
            rotateText.render(sb);
            sb.setProjectionMatrix(cam.combined);
            lmb.render(sb);
            rmb.render(sb);
        }

        Utils.drawCentered(sb, puzzleBg, Constants.WIDTH / 2f, Constants.HEIGHT / 2f, 126, 126);
        for (PuzzlePiece[] row : puzzle) {
            for (PuzzlePiece cell : row) cell.render(sb);
        }
        if (render2 != null) render2.render(sb);
        if (render1 != null) render1.render(sb);
        if (selected != null) {
            sb.setColor(Constants.PUZZLE_PIECE_BORDER);
            sb.draw(pixel, selected.x - selected.w / 2f - 1, selected.y - selected.h / 2f - 1, selected.w + 2, 1);
            sb.draw(pixel, selected.x - selected.w / 2f - 1, selected.y - selected.h / 2f - 1, 1, selected.h + 2);
            sb.draw(pixel, selected.x - selected.w / 2f - 1, selected.y + selected.h / 2f, selected.w + 2, 1);
            sb.draw(pixel, selected.x + selected.w / 2f, selected.y - selected.h / 2f - 1, 1, selected.h + 2);
        }

        sb.setColor(1, 1, 1, 1);
        if (done) {
            sb.setProjectionMatrix(textCam.combined);
            text.render(sb);
            if (doneTime < 0 && (nextTime % 0.9f) < 0.45f) {
                sb.setProjectionMatrix(uiCam.combined);
                sb.draw(next, 300, 10, 8, 8);
            }
        }

        sb.setProjectionMatrix(uiCam.combined);
        if (flash > 0) {
            sb.setColor(1, 1, 1, flash);
            sb.draw(pixel, 0, 0, Constants.WIDTH, Constants.HEIGHT);
        }

        sb.setColor(1, 1, 1, 1);
        in.render(sb);
        out.render(sb);

        sb.end();
    }
}
