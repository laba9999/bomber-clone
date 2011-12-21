package com.bomber.gamestates;

import com.bomber.Game;

public class GameStateRoundEndCTF extends GameStateRoundEnd {

	public GameStateRoundEndCTF(Game _game) {
		super(_game);
	}

	@Override
	protected final void onUpdateResults()
	{
		if (mTeam1.mCapturedEnemyFlag && mTeam2.mCapturedEnemyFlag)
		{
			//
			// Empate

			// Tenta desempatar por pontos
			if (mTeam1.getTotalPoints() == mTeam2.getTotalPoints())
			{

				// Não há hipotese de desempate
				mTeam1.mRoundsWon++;
				mTeam2.mRoundsWon++;

			} else if (mTeam1.getTotalPoints() > mTeam2.getTotalPoints())
			{
				// Equipa 1 ganha por pontos
				mTeam1.mRoundsWon++;
			} else
			{
				// Equipa 2 ganha por pontos
				mTeam2.mRoundsWon++;
			}

		} else if (mTeam1.mCapturedEnemyFlag)
		{
			// Team 1 ganhou
			mTeam1.mRoundsWon++;
		} else
		{
			// Team 2 ganhou
			mTeam2.mRoundsWon++;
		}
	}

}
