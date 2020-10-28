package me.skiincraft.discord.herostats.assets;

public enum Category {

    Statistics("Estatisticas"), Match("Partidas"), Ranking("Ranking"), Owner("Dono");

    private String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
