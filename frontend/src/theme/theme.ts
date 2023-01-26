import {createTheme} from "@mui/material";
import { deepPurple } from '@mui/material/colors';

const theme = createTheme({
    palette: {
        primary: {
            main: deepPurple[700],
        },
    },
});

export default theme;