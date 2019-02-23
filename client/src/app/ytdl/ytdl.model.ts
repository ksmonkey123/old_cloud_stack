export interface Format {
    id: number;
    name: string;
    suffix: string;
    category: string;
}

export interface JobBase {
    id: number;
    status: string;
}

export interface JobSummary extends JobBase {
    id: number;
    status: string;
    url: string;
    formats: number;
    files: number;
    formatList: string;
    name: string;
}

export interface FormatListEntry {
    category: string;
    formats: Format[];
}

export interface OutputFile {
    id: number;
    size: number;
    format: Format;
    path: string;
}

export interface JobDetails extends JobBase {
    id: number;
    status: string;
    url: string;
    formats: Format[];
    files: OutputFile[];
    name: string;
}