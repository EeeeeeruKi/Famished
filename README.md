# Famished

空腹になると、プレイヤーが弱くなる Minecraft の Mod です。

> このmodは大部分または全てAIによって作成されました。

## 効果

プレイヤーの満腹度に応じて、エンティティへの攻撃リーチ（`forge:entity_reach`）が短くなります。

| 満腹度 | リーチの変化 |
| --- | --- |
| 7以上 | 変化なし |
| 3〜6 | -0.5 |
| 0〜2 | -1.0 |

クリエイティブモードとスペクテイターモードでは影響を受けません。

## 対応環境

- Minecraft 1.20.1
- Forge 47.3.0 以降

## 導入方法

1. [Releases](../../releases) から `famished-1.0.0.jar` をダウンロードします。
2. ダウンロードした jar を、Minecraft の `mods` フォルダに入れます。
3. Forge 1.20.1 でゲームを起動します。

## ビルド方法

Java 17 と Gradle が必要です。

```
gradle build
```

`build/libs` に jar が作られます。

## ライセンス

[MIT License](LICENSE)
