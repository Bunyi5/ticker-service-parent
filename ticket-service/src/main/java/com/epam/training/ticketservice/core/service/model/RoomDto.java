package com.epam.training.ticketservice.core.service.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RoomDto {

    private final Long id;
    private final String roomName;
    private final int roomSeat;
    private final int roomRow;
    private final int roomColumn;

    public static RoomDtoBuilder builder() {
        return new RoomDtoBuilder();
    }

    @Override
    public String toString() {
        return "Room " + roomName + " with " + roomSeat + " seats, " + roomRow + " rows and " + roomColumn + " columns";
    }

    public static class RoomDtoBuilder {
        private Long id;
        private String roomName;
        private int roomSeat;
        private int roomRow;
        private int roomColumn;

        RoomDtoBuilder() {
        }

        public RoomDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public RoomDtoBuilder roomName(String roomName) {
            this.roomName = roomName;
            return this;
        }

        public RoomDtoBuilder roomRow(int roomRow) {
            this.roomRow = roomRow;
            updateRoomSeat();
            return this;
        }

        public RoomDtoBuilder roomColumn(int roomColumn) {
            this.roomColumn = roomColumn;
            updateRoomSeat();
            return this;
        }

        private void updateRoomSeat() {
            this.roomSeat = this.roomRow * this.roomColumn;
        }

        public RoomDto build() {
            return new RoomDto(id, roomName, roomSeat, roomRow, roomColumn);
        }

        public String toString() {
            return "RoomDto.RoomDtoBuilder(roomSeat=" + this.roomSeat + ", roomRow="
                    + this.roomRow + ", roomColumn=" + this.roomColumn + ", roomName=" + this.roomName + ")";
        }
    }
}
