export interface Format {
    id: number;
    name: string;
    suffix: string;
    category: string;
}

export interface JobSummary {
    id: number;
    status: string;
    url: string;
    formats: number;
    files: number;   
}

export interface FormatListEntry {
    category: string;
    formats: Format[];
}

