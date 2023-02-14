import {createTheme} from "@mui/material";
import { deepPurple } from '@mui/material/colors';

const theme = createTheme({
    palette: {
        primary: {
            main: deepPurple[700],
        },
    },
    typography: {
        fontSize: 12,
        h1: {
            fontSize: 30
        },
        h2: {
            fontSize: 20
        },
        h3: {
            fontSize: 15,
            fontWeight: 900
        },
    },
    components: {
        MuiInputBase: {
            styleOverrides: {
                root: {
                    fontSize: 16,
                }
            }
        }
    }
});

export default theme;