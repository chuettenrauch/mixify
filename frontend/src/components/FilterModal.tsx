import {
    Button,
    Container, FormControl,
    IconButton, InputLabel, MenuItem,
    Modal,
    Paper, Select,
    Stack,
    Typography
} from "@mui/material";
import React, {FormEvent, useState} from "react";
import {Close as CloseIcon, FilterList as FilterListIcon} from "@mui/icons-material";
import Filter, {CreatedByFilter, DraftFilter} from "../types/filter";

export default function FilterModal({open, onFilter, onClear, onClose}: {
    open: boolean,
    onFilter: (filters: Filter[]) => void,
    onClear: () => void,
    onClose: () => void,
}) {
    const [createdByFilterSelect, setCreatedByFilterSelect] = useState<string>("");
    const [draftFilterSelect, setDraftFilterSelect] = useState<string>("");

    const onSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const filters: Filter[] = [];

        const createdByFilter = CreatedByFilter.get(createdByFilterSelect);
        if (createdByFilter) {
            filters.push(createdByFilter);
        }

        const draftFilter = DraftFilter.get(draftFilterSelect);
        if (draftFilter) {
            filters.push(draftFilter);
        }

        onFilter(filters);
        onClose();
    }

    const clearFilter = () => {
        setCreatedByFilterSelect("");
        setDraftFilterSelect("");

        onClear();
        onClose();
    }

    return (
        <Modal
            open={open}
            aria-labelledby="modal-mixtape-filter-form"
            aria-describedby="Form to filter mixtapes"
            sx={{zIndex: (theme) => theme.zIndex.appBar - 2}}
        >
            <Paper elevation={5} sx={{
                position: "absolute",
                top: '30%',
                left: '50%',
                transform: 'translate(-50%, -30%)',
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                gap: 2,
                width: "min(85%, 450px)",
                p: 2
            }}>
                <Container sx={{display: "flex", justifyContent: "center", position: "relative"}}>
                    <Typography variant="h2" textTransform={"uppercase"}>Filter</Typography>
                    <IconButton sx={{position: "absolute", top: -8, right: -8}} onClick={() => onClose()}>
                        <CloseIcon/>
                    </IconButton>
                </Container>

                <Stack
                    component="form"
                    spacing={3}
                    width="100%"
                    maxWidth={(theme) => theme.breakpoints.values.sm}
                    onSubmit={onSubmit}
                >
                    <FormControl variant="standard">
                        <InputLabel>Created by</InputLabel>
                        <Select
                            label="Created by"
                            value={createdByFilterSelect}
                            onChange={e => setCreatedByFilterSelect(e.target.value)}
                        >
                            <MenuItem value="">
                                <em>None</em>
                            </MenuItem>
                            <MenuItem value="user">You</MenuItem>
                            <MenuItem value="other">Other</MenuItem>
                        </Select>
                    </FormControl>

                    <FormControl variant="standard">
                        <InputLabel>Draft</InputLabel>
                        <Select
                            label="Draft"
                            value={draftFilterSelect}
                            onChange={e => setDraftFilterSelect(e.target.value)}
                        >
                            <MenuItem value="">
                                <em>None</em>
                            </MenuItem>
                            <MenuItem value="yes">Yes</MenuItem>
                            <MenuItem value="no">No</MenuItem>
                        </Select>
                    </FormControl>

                    <Button variant="outlined" onClick={() => clearFilter()}>
                        Clear all
                    </Button>

                    <Button type="submit" variant="contained" startIcon={<FilterListIcon/>}>
                        Filter
                    </Button>
                </Stack>
            </Paper>
        </Modal>
    )
}