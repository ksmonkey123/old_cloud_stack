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
    formatList: string;
}

export interface FormatListEntry {
    category: string;
    formats: Format[];
}

export interface OutputFile {
    id: number;
    size: number;
    format: Format;
    
}

export interface JobDetails {
    id: number;
    status: string;
    url: string;
    formats: Format[];
    files: OutputFile[];
}