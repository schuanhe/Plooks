export interface CollectionType {
    id: number,
    name?: string,
    cover?: string,
    desc?: string,
    open?: boolean,
    createdAt?: string
}

export interface ModifyCollectionType {
    id: number,
    name: string,
    cover: string,
    desc: string,
    open: boolean,
}

export interface CollectionInfoType {
    id: number,
    name: string,
    cover: string,
    desc: string,
    open: boolean,
    createdAt: string
}