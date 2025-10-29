package com.distraction.comfyjam1025.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.comfyjam1025.Constants;
import com.distraction.comfyjam1025.Context;
import com.distraction.comfyjam1025.entity.ImageEntity;
import com.distraction.comfyjam1025.entity.PuzzlePiece;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayScreen extends Screen {

    private static final int ROWS = 3;

    private final ImageEntity select;
    private final TextureRegion puzzleBg;
    private final PuzzlePiece[][] puzzle;

    private PuzzlePiece selected;

    public PlayScreen(Context context, int year) {
        super(context);

        select = new ImageEntity(context, context.getImage("selected"));
        puzzleBg = context.getImage("puzzlebg");
        puzzle = new PuzzlePiece[ROWS][ROWS];
        int tileSize = 120 / ROWS;
        float sx = Constants.WIDTH / 2f - 60;
        float sy = Constants.HEIGHT / 2f + 60;
        TextureRegion[][] images = context.getImage("puzzle" + year).split(tileSize, tileSize);
        for (int row = 0; row < puzzle.length; row++) {
            for (int col = 0; col < puzzle[row].length; col++) {
                puzzle[row][col] = new PuzzlePiece(context, row * ROWS + col, row, col, images[row][col]);
            }
        }

        List<PuzzlePiece> flat = new ArrayList<>();
        for (PuzzlePiece[] row : puzzle) {
            for (PuzzlePiece cell : row) flat.add(cell);
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

    }

    private void updatePositions() {
        int tileSize = 120 / ROWS;
        float sx = Constants.WIDTH / 2f - 60;
        float sy = Constants.HEIGHT / 2f + 60;
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

    private boolean isDone() {
        for (int row = 0; row < puzzle.length; row++) {
            for (int col = 0; col < puzzle[row].length; col++) {
                if (puzzle[row][col].id != row * ROWS + col || puzzle[row][col].rotateIndex != 0) {
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
            for (PuzzlePiece[] row : puzzle) {
                for (PuzzlePiece cell : row) {
                    if (cell.contains(m.x, m.y)) {
                        if (selected == null) {
                            selected = cell;
                            select.x = selected.x;
                            select.y = selected.y;
                        } else if (selected == cell) {
                            selected = null;
                        } else {
                            int r1 = selected.row;
                            int c1 = selected.col;
                            int r2 = cell.row;
                            int c2 = cell.col;

                            PuzzlePiece temp = puzzle[r1][c1];
                            puzzle[r1][c1] = puzzle[r2][c2];
                            puzzle[r1][c1].setDest(temp.x, temp.y);
                            puzzle[r2][c2] = temp;
                            puzzle[r2][c2].setDest(puzzle[r1][c1].x, puzzle[r1][c1].y);
                            updatePositions();
                            selected = null;
                        }
                        return;
                    }
                }
            }
        } else if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            for (PuzzlePiece[] row : puzzle) {
                for (PuzzlePiece cell : row) {
                    if (cell.contains(m.x, m.y)) {
                        cell.rotate();
                    }
                }
            }
        }
    }

    @Override
    public void update(float dt) {
        in.update(dt);
        out.update(dt);

        for (PuzzlePiece[] row : puzzle) {
            for (PuzzlePiece cell : row) cell.update(dt);
        }

        if (isDone()) {
        }
    }

    @Override
    public void render() {
        sb.begin();

        sb.setProjectionMatrix(cam.combined);
        sb.setColor(Constants.PUZZLE_BG);
        sb.draw(pixel, 0, 0, Constants.WIDTH, Constants.HEIGHT);

        sb.setColor(1, 1, 1, 1);
        sb.draw(puzzleBg, Constants.WIDTH / 2f - puzzleBg.getRegionWidth() / 2f, Constants.HEIGHT / 2f - puzzleBg.getRegionHeight() / 2f);

        for (PuzzlePiece[] row : puzzle) {
            for (PuzzlePiece cell : row) cell.render(sb);
        }

        if (selected != null) select.render(sb);

        in.render(sb);
        out.render(sb);

        sb.end();
    }
}
